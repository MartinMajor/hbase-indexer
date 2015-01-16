/*
 * Copyright 2013 NGDATA nv
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ngdata.sep.tools.monitoring;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.ngdata.sep.util.io.Closer;
import com.ngdata.sep.util.zookeeper.ZkUtil;
import com.ngdata.sep.util.zookeeper.ZooKeeperItf;


@Path("indexer")
public class ReplicationStatusResource {

    @GET
    @Path("replication-status")
    @Produces("application/json")
    public ReplicationStatus get(@Context UriInfo uriInfo) throws Exception {
        ZooKeeperItf zk = ZkUtil.connect("localhost", 30000);
        ReplicationStatusRetriever retriever = new ReplicationStatusRetriever(zk, 60010);
        ReplicationStatus replicationStatus = retriever.collectStatusFromZooKeepeer();
        retriever.addStatusFromJmx(replicationStatus);
//        ReplicationStatusReport.printReport(replicationStatus, System.out);
        Closer.close(zk);
        return replicationStatus;
    }
	
}
