package module.webservice.api;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import module.finance.domain.SupplierContact;
import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.mission.domain.RemoteMissionProcess;
import module.mission.domain.RemoteMissionSystem;
import module.mission.domain.activity.AssociateMissionProcessActivity;
import module.mission.domain.activity.AssociateMissionProcessActivityInfo;
import module.mission.domain.activity.DisassociateMissionProcessActivity;
import module.mission.domain.activity.DisassociateMissionProcessActivityInfo;
import module.mission.domain.util.SearchMissions;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowSystem;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.bennu.core._development.PropertiesManager;
import pt.ist.bennu.core.applicationTier.Authenticate;
import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.DeleteAfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.EditAfterTheFactProcessActivityInformation.AfterTheFactAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CancelAcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("expenditures-tracking/v1")
public class ExpenditureAPIv1 {

    private static final Logger logger = LoggerFactory.getLogger(ExpenditureAPIv1.class);

    public final static String JSON_UTF8 = "application/json; charset=utf-8";

    DateTimeFormatter formatDayHour = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

    public static final DateTimeFormatter formatDay = DateTimeFormat.forPattern("dd/MM/yyyy");
    public static final SimpleDateFormat dataFormatDay = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat dataFormatHour = new SimpleDateFormat("HH:mm");

    private static Gson gson;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @GET
    @Produces(JSON_UTF8)
    @Path("suppliers")
    public String suppliers(@QueryParam("userID") String userID, @QueryParam("access_token") String access_token) {
        checkToken(access_token);
        Set<Supplier> suppliers = MyOrg.getInstance().getSuppliersSet();
        JsonArray toReturn = new JsonArray();
        for (Supplier supplier : suppliers) {
            if (supplier != null) {
                JsonObject obj = new JsonObject();

                obj.addProperty("supplierID", supplier.getExternalId());
                obj.addProperty("fiscalID", supplier.getFiscalIdentificationCode());
                obj.addProperty("name", supplier.getName());
                obj.addProperty("shortName", supplier.getAbbreviatedName());
                obj.addProperty("limit", supplier.getSupplierLimit().toFormatString());
                JsonArray contacts = new JsonArray();
                for (SupplierContact contact : supplier.getSupplierContactSet()) {
                    JsonObject contactObj = new JsonObject();
                    if (contact.getAddress() != null) {
                        JsonObject addressObj = new JsonObject();
                        addressObj.addProperty("line1", contact.getAddress().getLine1());
                        addressObj.addProperty("line2", contact.getAddress().getLine2());
                        addressObj.addProperty("country", contact.getAddress().getCountry());
                        contactObj.add("address", addressObj);
                    }
                    contactObj.addProperty("phone", contact.getPhone());
                    contactObj.addProperty("fax", contact.getFax());
                    contactObj.addProperty("email", contact.getEmail());
                    contacts.add(contactObj);
                }
                obj.add("contacts", contacts);
                obj.addProperty("totalAllocated", supplier.getTotalAllocated().toFormatString());
                JsonArray byCpv = new JsonArray();
                for (CPVReference cpv : supplier.getAllocationsByCPVReference().keySet()) {
                    if (cpv != null) {
                        JsonObject cpvObj = new JsonObject();
                        cpvObj.addProperty("cpvCode", cpv.getCode());
                        cpvObj.addProperty("cpvDescription", cpv.getDescription());
                        cpvObj.addProperty("totalAllocated", supplier.getTotalAllocated(cpv).toFormatString());
                        byCpv.add(cpvObj);;

                    }
                }
                obj.add("allocationsByCPV", byCpv);
                toReturn.add(obj);
            }
        }

        return gson.toJson(toReturn);
    }

    @POST
    @Produces(JSON_UTF8)
    @Path("allocateFunds")
    public String allocateFunds(@QueryParam("supplierID") String supplierID, @QueryParam("value") String value,
            @QueryParam("valueVat") String valueVAT, @QueryParam("cpvCode") String cpvcode,
            @QueryParam("goodsOrService") String goodsOrServices, @QueryParam("description") String description,
            @QueryParam("userID") String userID, @QueryParam("access_token") String access_token) {

        checkToken(access_token);
        login(User.findByUsername(userID));
        try {

            AfterTheFactAcquisitionProcessBean bean = new AfterTheFactAcquisitionProcessBean();

            Set<Supplier> suppliers = MyOrg.getInstance().getSuppliersSet();
            Supplier supplier = null;

            for (Supplier sup : suppliers) {
                if (sup.getExternalId().equals(supplierID)) {
                    supplier = sup;
                    break;
                }
            }
            if (supplier == null) {
                throw newApplicationError(Status.NOT_FOUND, "resource_not_found", "Can't find the supplier : " + supplierID);
            }

            bean.setSupplier(supplier);
            bean.setAfterTheFactAcquisitionType(AfterTheFactAcquisitionType.PURCHASE);

            Money itemValue = new Money(value);
            bean.setValue(itemValue);

            double VAT = Double.parseDouble(valueVAT);
            bean.setVatValue(new BigDecimal(VAT));

            bean.setYear(new LocalDate().getYear());
            bean.setDescription(description);
            bean.setClassification(AcquisitionItemClassification.valueOf(goodsOrServices.toUpperCase()));
            CPVReference cpvReference = CPVReference.getCPVCode(cpvcode);
            if (cpvReference == null) {
                throw newApplicationError(Status.NOT_FOUND, "resource_not_found", "Can't find the cpv code : " + cpvcode);
            }
            bean.setCpvReference(cpvReference);

            AfterTheFactAcquisitionProcess process;

            try {
                process = AfterTheFactAcquisitionProcess.createNewAfterTheFactAcquisitionProcess(bean);
            } catch (DomainException e) {
                throw newApplicationError(Status.PRECONDITION_FAILED, "precondition_failed", e.getLocalizedMessage());
            }
            JsonObject obj = new JsonObject();
            obj.addProperty("processID", process.getProcessNumber());

            return gson.toJson(obj);
        } finally {
            logout();
        }
    }

    //change to put
    @PUT
    @Produces(JSON_UTF8)
    @Path("cancelFundAllocation")
    public String cancelFundAllocation(@QueryParam("processID") String processID, @QueryParam("userID") String userID,
            @QueryParam("access_token") String access_token) {
        checkToken(access_token);
        login(User.findByUsername(userID));
        try {
            WorkflowSystem ws = WorkflowSystem.getInstance();
            Set<WorkflowProcess> processes = ws.getProcessesSet();
            for (WorkflowProcess workflowProcess : processes) {
                if (workflowProcess.getProcessNumber() != null) {
                    if (workflowProcess.getProcessNumber().equals(processID)) {
                        WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> cancelAcquisitionRequest =
                                workflowProcess.getActivity(DeleteAfterTheFactAcquisitionProcess.class.getSimpleName());

                        if (cancelAcquisitionRequest == null) { //is not after the fact
                            cancelAcquisitionRequest =
                                    workflowProcess.getActivity(CancelAcquisitionRequest.class.getSimpleName());
                        }
                        try {
                            cancelAcquisitionRequest.execute(new ActivityInformation<WorkflowProcess>(workflowProcess,
                                    cancelAcquisitionRequest));
                        } catch (Exception e) {
                            throw newApplicationError(Status.NOT_ACCEPTABLE, "cancelation_not_acceptable",
                                    e.getLocalizedMessage());
                        }
                        JsonObject obj = new JsonObject();
                        obj.addProperty("status", Status.OK.toString());
                        return gson.toJson(obj);
                    }
                }
            }
            //No process was found
            throw newApplicationError(Status.NOT_FOUND, "resource_not_found", "Can't find the process : " + processID);
        } finally {
            logout();
        }
    }

    @POST
    @Produces(JSON_UTF8)
    @Path("connectMissionProcess")
    public String connectMissionProcess(@QueryParam("processNumber") String processNumber,
            @QueryParam("externalId") String externalId, @QueryParam("hostname") String hostname,
            @QueryParam("remoteProcessNumber") String remoteProcessNumber, @QueryParam("username") String username,
            @QueryParam("access_token") String access_token) {

        checkToken(access_token);
        try {
            login(User.findByUsername(username));
            final Mission mission = findMission(remoteProcessNumber);
            if (mission == null) {
                throw newApplicationError(Status.BAD_REQUEST, "bad_request", "Bad process number: " + remoteProcessNumber);
            } else {
                try {
                    VirtualHost.setVirtualHostForThread(mission.getMissionSystem().getVirtualHostSet().iterator().next());
                    final MissionProcess missionProcess = mission.getMissionProcess();

                    final RemoteMissionSystem remoteMissionSystem = RemoteMissionSystem.find(hostname);
                    if (remoteMissionSystem == null) {
                        throw newApplicationError(Status.NOT_ACCEPTABLE, "not_acceptable",
                                "Host not allowed to connect to processes.");
                    }

                    final AssociateMissionProcessActivity activity =
                            (AssociateMissionProcessActivity) missionProcess.getActivity(AssociateMissionProcessActivity.class);
                    final AssociateMissionProcessActivityInfo information = activity.getActivityInformation(missionProcess);
                    information.setProcessNumber(processNumber);
                    information.setExternalId(externalId);
                    information.setRemoteMissionSystem(remoteMissionSystem);
                    information.setConnect(false);
                    activity.execute(information);

                    final JsonObject obj = new JsonObject();
                    obj.addProperty("processID", missionProcess.getProcessNumber());
                    obj.addProperty("externalId", missionProcess.getExternalId());
                    return gson.toJson(obj);
                } finally {
                    VirtualHost.releaseVirtualHostFromThread();
                }
            }
        } finally {
            logout();
        }
    }

    private Mission findMission(final String remoteProcessNumber) {
        try {
            final SearchMissions search = new SearchMissions();
            search.setProcessNumber(remoteProcessNumber);
            for (final VirtualHost virtualHost : MyOrg.getInstance().getVirtualHostsSet()) {
                VirtualHost.setVirtualHostForThread(virtualHost);
                final Set<Mission> missions = search.search();
                if (missions.size() == 1) {
                    return missions.iterator().next();
                }
            }
        } finally {
            VirtualHost.releaseVirtualHostFromThread();
        }
        return null;
    }

    @POST
    @Produces(JSON_UTF8)
    @Path("disconnectMissionProcess")
    public String disconnectMissionProcess(@QueryParam("processNumber") String processNumber,
            @QueryParam("hostname") String hostname, @QueryParam("remoteProcessNumber") String remoteProcessNumber,
            @QueryParam("username") String username, @QueryParam("access_token") String access_token) {

        checkToken(access_token);
        login(User.findByUsername(username));
        try {
            final Mission mission = findMission(remoteProcessNumber);
            if (mission == null) {
                throw newApplicationError(Status.BAD_REQUEST, "bad_request", "Bad process number: " + remoteProcessNumber);
            } else {
                try {
                    VirtualHost.setVirtualHostForThread(mission.getMissionSystem().getVirtualHostSet().iterator().next());

                    final MissionProcess missionProcess = mission.getMissionProcess();

                    final RemoteMissionSystem remoteMissionSystem = RemoteMissionSystem.find(hostname);
                    if (remoteMissionSystem == null) {
                        throw newApplicationError(Status.NOT_ACCEPTABLE, "not_acceptable",
                                "Host not allowed to connect to processes.");
                    }

                    for (final RemoteMissionProcess remoteMissionProcess : missionProcess.getRemoteMissionProcessSet()) {
                        if (remoteMissionProcess.getRemoteMissionSystem() == remoteMissionSystem
                                && remoteMissionProcess.getProcessNumber().equals(processNumber)) {
                            final DisassociateMissionProcessActivity activity =
                                    (DisassociateMissionProcessActivity) missionProcess
                                            .getActivity(DisassociateMissionProcessActivity.class);
                            final DisassociateMissionProcessActivityInfo information =
                                    activity.getActivityInformation(missionProcess);
                            information.setRemoteMissionProcess(remoteMissionProcess);
                            information.setConnect(false);
                            activity.execute(information);
                        }
                    }

                    final JsonObject obj = new JsonObject();
                    obj.addProperty("status", "OK");
                    return gson.toJson(obj);
                } finally {
                    VirtualHost.releaseVirtualHostFromThread();
                }
            }
        } finally {
            logout();
        }
    }

    private void checkToken(String token) {
        String storedToken = PropertiesManager.getProperty("expenditures.api.token");
        if (!storedToken.equals(token)) {
            throw newApplicationError(Status.FORBIDDEN, "can't access resource", "you're not able to use this service");
        }
    }

    private void login(User user) {
        if (user == null) {
            throw newApplicationError(Status.BAD_REQUEST, "no user found", "a user valid user must be specified");
        }
        final UserView userView = Authenticate.authenticate(user);
        pt.ist.fenixWebFramework.security.UserView.setUser(userView);
    }

    private void logout() {
        pt.ist.fenixWebFramework.security.UserView.setUser(null);
    }

    private WebApplicationException newApplicationError(Status status, String error, String description) {
        JSONObject errorObject = new JSONObject();
        errorObject.put("error", error);
        errorObject.put("description", description);
        return new WebApplicationException(Response.status(status).entity(errorObject.toJSONString()).build());
    }

}
