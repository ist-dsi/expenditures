/*
 * @(#)WorkingCapitalTransactionFiles.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.presentationTier.renderers;

import java.util.Iterator;
import java.util.Set;

import module.workingCapital.domain.TransactionFile;
import module.workingCapital.domain.WorkingCapitalTransaction;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlParagraphContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlScript;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class WorkingCapitalTransactionFiles extends OutputRenderer {

    private String downloadFormat;
    private String removeFormat;

    public String getDownloadFormat() {
        return downloadFormat;
    }

    public void setDownloadFormat(String downloadFormat) {
        this.downloadFormat = downloadFormat;
    }

    public String getRemoveFormat() {
        return removeFormat;
    }

    public void setRemoveFormat(String removeFormat) {
        this.removeFormat = removeFormat;
    }

    @Override
    protected Layout getLayout(Object arg0, Class arg1) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object arg0, Class arg1) {
                WorkingCapitalTransaction transaction = (WorkingCapitalTransaction) arg0;

                HtmlBlockContainer container = new HtmlBlockContainer();
                container.addChild(generate(transaction));
                return container;
            }

            private HtmlComponent generate(WorkingCapitalTransaction transaction) {
                Set<TransactionFile> files = transaction.getTransactionFileSet();
                HtmlBlockContainer blockContainer = new HtmlBlockContainer();
                HtmlParagraphContainer container = new HtmlParagraphContainer();
                blockContainer.addChild(container);
                if (!files.isEmpty()) {

                    Iterator<? extends TransactionFile> iterator = files.iterator();

                    while (iterator.hasNext()) {
                        TransactionFile file = iterator.next();

                        HtmlLink downloadLink = new HtmlLink();
                        String filename = file.getDisplayName();
                        if (StringUtils.isEmpty(filename)) {
                            filename = file.getFilename();
                        }
                        //joantune: taking care of the confirmation action in case this is 
                        //a file whose access is logged
                        downloadLink.setId("access-" + file.getExternalId());
                        downloadLink.setIndented(false);
                        downloadLink.setBody(new HtmlText(filename));
                        downloadLink.setUrl(RenderUtils.getFormattedProperties(getDownloadFormat(), file));
                        container.addChild(downloadLink);
                        if (file.shouldFileContentAccessBeLogged()) {
                            container.addChild(accessConfirmation(file));
                        }

                        if (file.isPossibleToArchieve() && file.getProcess().isFileEditionAllowed()) {

                            HtmlLink removeLink = new HtmlLink();
                            removeLink.setIndented(false);
                            removeLink.setId("remove-" + file.getExternalId());
                            removeLink.setBody(new HtmlText("("
                                    + RenderUtils.getResourceString("WORKFLOW_RESOURCES", "link.removeFile") + ")"));
                            removeLink.setUrl(RenderUtils.getFormattedProperties(getRemoveFormat(), file));

                            container.addChild(removeLink);
                            container.addChild(removeConfirmation(file));
                        }

                        if (iterator.hasNext()) {
                            container.addChild(new HtmlText(", "));
                        }
                    }
                }
                return blockContainer;

            }

            private HtmlComponent accessConfirmation(TransactionFile file) {
                HtmlScript script = new HtmlScript();
                script.setContentType("text/javascript");
                String displayName = file.getDisplayName();
                if (displayName == null) {
                    displayName = file.getFilename();
                }
                script.setScript("linkConfirmationHookLink('access-"
                        + file.getExternalId()
                        + "', '"
                        + BundleUtil.getFormattedStringFromResourceBundle("resources/WorkflowResources",
                                "label.fileAccess.logged.confirmMessage", displayName) + "' , '" + displayName + "');");
                return script;
            }

            private HtmlComponent removeConfirmation(TransactionFile file) {
                HtmlScript script = new HtmlScript();
                script.setContentType("text/javascript");
                String displayName = file.getDisplayName();
                if (displayName == null) {
                    displayName = file.getFilename();
                }
                script.setScript("linkConfirmationHook('remove-"
                        + file.getExternalId()
                        + "', '"
                        + BundleUtil.getFormattedStringFromResourceBundle("resources/WorkflowResources",
                                "label.fileRemoval.confirmation", displayName) + "' , '" + displayName + "');");
                return script;
            }

        };
    }
}
