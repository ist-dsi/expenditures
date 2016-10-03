package module.webservice.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.fenixedu.bennu.core.rest.BennuRestResource;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import module.mission.domain.Mission;
import module.mission.presentationTier.dto.SearchMissionsDTO;
import module.webservice.api.json.AbbreviatedMissionAdapter;

@Path("mission/v1")
public class MissionResource extends BennuRestResource {

    public final static String SCHEDULE_SCOPE = "EXPENDITURE";

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @OAuthEndpoint(SCHEDULE_SCOPE)
    public JsonElement index(@Context HttpServletRequest request, @QueryParam(value = "page") final String pageStr) {
        // parse page number
        int page;
        try {
            page = (pageStr == null) ? 0 : Integer.parseInt(pageStr);

            if (page < 0) {
                page = 0;
            }
        } catch (final NumberFormatException e) {
            throw new WebApplicationException(400);
        }
        final SearchMissionsDTO searchMissions = new SearchMissionsDTO(request);
        final List<Mission> missions = searchMissions.sortedSearch();

        final int ipp = 10;
        int skip = page * ipp;
        final int total = missions.size();
        final int totalPages = (total + ipp - 1) / ipp;        //CEIL TOTAL PAGES
        if (page >= totalPages && totalPages > 0) {
            // load last page instead
            page = totalPages - 1;
            skip = page * ipp;
        }

        final JsonObject json;
        if (missions.isEmpty()) {
            json = super.view(missions, "missions", AbbreviatedMissionAdapter.class).getAsJsonObject();
        } else {
            json = super.viewPaginated(missions, "missions", AbbreviatedMissionAdapter.class, skip, ipp).getAsJsonObject();
        }
        json.addProperty("totalPages", totalPages);
        json.addProperty("currentPage", page);
        json.addProperty("itemsPerPage", ipp);
        json.addProperty("sortBy", request.getParameter("sortBy"));
        return json;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @OAuthEndpoint(SCHEDULE_SCOPE)
    public JsonElement view(@QueryParam("processNumber") String processNumber) {
        if (processNumber == null || processNumber.isEmpty()) {
            throw new WebApplicationException(400);
        }

        List<Mission> missions;
        try {
            final SearchMissionsDTO searchMissions = new SearchMissionsDTO();
            searchMissions.setProcessNumber(processNumber);
            missions = searchMissions.sortedSearch();
        } catch (final Exception e) {
            throw new WebApplicationException(404);
        }

        if (missions.isEmpty() || missions.size() > 1) {
            throw new WebApplicationException(404);
        }

        final Mission mission = missions.get(0);
        return super.view(mission).getAsJsonObject();
    }
}
