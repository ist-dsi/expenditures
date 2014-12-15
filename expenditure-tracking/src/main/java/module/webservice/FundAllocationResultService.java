/*
 * @(#)FundAllocationResultService.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the External Accounting Integration Module.
 *
 *   The External Accounting Integration Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The External Accounting Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the External Accounting Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import pt.ist.fenixframework.Atomic;

@Path("/fundAllocationResultService")
/**
 * 
 * @author Luis Cruz
 * 
 */
public class FundAllocationResultService {

    /**
     * Register fund allocation request result
     * 
     * @author DSI @ IST
     * @param id FundAllocationRequestId
     * @param fundAllocationNumber Fund allocation number
     * @param operatorUsername Username of operator who allocated the funds
     * @return register status
     */
    @GET
    @Path("registerResult/{id}/{fundAllocationNumber}/{operatorUsername}")
    @Produces("text/plain")
    public Response registerResult(@PathParam("id") final String id,
            @PathParam("fundAllocationNumber") final String fundAllocationNumber,
            @PathParam("operatorUsername") final String operatorUsername) {
        registerResultService(id, fundAllocationNumber, operatorUsername);
        return Response.ok("ok", "text/plain").build();
    }

    @Atomic
    public void registerResultService(final String id, final String fundAllocationNumber, final String operatorUsername) {
        throw new Error();
//        final FundAllocationRequest fundAllocationRequest = FenixFramework.getDomainObject(id);
//        fundAllocationRequest.registerFundAllocation(fundAllocationNumber, operatorUsername);
    }

}
