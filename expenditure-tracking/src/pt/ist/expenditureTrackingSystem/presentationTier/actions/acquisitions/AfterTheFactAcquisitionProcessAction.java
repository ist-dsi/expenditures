/*
 * @(#)AfterTheFactAcquisitionProcessAction.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.presentationTier.actions.ProcessManagement;
import myorg.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.ImportFile;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities.EditAfterTheFactProcessActivityInformation.AfterTheFactAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionsImportBean;
import pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionsImportBean.ImportError;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.plugins.fileSupport.domain.GenericFile;

@Mapping(path = "/acquisitionAfterTheFactAcquisitionProcess")
/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AfterTheFactAcquisitionProcessAction extends BaseAction {

    public ActionForward prepareCreateAfterTheFactAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean = new AfterTheFactAcquisitionProcessBean();
	request.setAttribute("afterTheFactAcquisitionProcessBean", afterTheFactAcquisitionProcessBean);
	return forward(request, "/acquisitions/createAfterTheFactAcquisitionProcess.jsp");
    }

    public ActionForward createNewAfterTheFactAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean = getRenderedObject();
	final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess;
	try {
	    afterTheFactAcquisitionProcess = AfterTheFactAcquisitionProcess
		    .createNewAfterTheFactAcquisitionProcess(afterTheFactAcquisitionProcessBean);
	} catch (DomainException e) {
	    addMessage(request, e.getMessage());
	    return prepareCreateAfterTheFactAcquisitionProcess(mapping, form, request, response);
	}
	return ProcessManagement.forwardToProcess(afterTheFactAcquisitionProcess);
    }

    public ActionForward prepareImport(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final AfterTheFactAcquisitionsImportBean afterTheFactAcquisitionsImportBean = new AfterTheFactAcquisitionsImportBean();
	request.setAttribute("afterTheFactAcquisitionsImportBean", afterTheFactAcquisitionsImportBean);
	return forward(request, "/acquisitions/importAfterTheFactAcquisitions.jsp");
    }

    public ActionForward processImport(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final AfterTheFactAcquisitionsImportBean afterTheFactAcquisitionsImportBean = getRenderedObject();
	final byte[] contents = consumeInputStream(afterTheFactAcquisitionsImportBean);
	afterTheFactAcquisitionsImportBean.setFileContents(contents);
	afterTheFactAcquisitionsImportBean.setCreateData(false);
	afterTheFactAcquisitionsImportBean.importAcquisitions();
	request.setAttribute("afterTheFactAcquisitionsImportBean", afterTheFactAcquisitionsImportBean);
	return forward(request, "/acquisitions/viewImportAfterTheFactAcquisitionsResult.jsp");
    }

    public ActionForward importAcquisitions(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final AfterTheFactAcquisitionsImportBean afterTheFactAcquisitionsImportBean = getRenderedObject();
	try {
	    afterTheFactAcquisitionsImportBean.reset();
	    afterTheFactAcquisitionsImportBean.setCreateData(true);
	    afterTheFactAcquisitionsImportBean.importAcquisitions();
	} catch (ImportError ex) {
	    // just show the page...
	}
	request.setAttribute("afterTheFactAcquisitionsImportBean", afterTheFactAcquisitionsImportBean);
	return forward(request, "/acquisitions/viewImportAfterTheFactAcquisitionsResult.jsp");
    }

    public ActionForward listImports(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	List<ImportFile> files = GenericFile.getFiles(ImportFile.class);
	request.setAttribute("files", files);
	return forward(request, "/acquisitions/listImportAfterTheFactAcquisitionsResult.jsp");
    }

    public ActionForward downloadImportFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws IOException {

	ImportFile file = getDomainObject(request, "fileOID");
	return download(response, file);
    }

    public ActionForward cancelImportFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	ImportFile file = getDomainObject(request, "fileOID");
	file.cancel();

	return listImports(mapping, form, request, response);
    }

    public ActionForward enableImportFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {

	ImportFile file = getDomainObject(request, "fileOID");
	file.reenable();

	return listImports(mapping, form, request, response);
    }

}
