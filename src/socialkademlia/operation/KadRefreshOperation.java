package socialkademlia.operation;

import java.io.IOException;
import kademlia.KadConfiguration;
import kademlia.KadServer;
import kademlia.operation.Operation;
import socialkademlia.SocialKademliaNode;
import socialkademlia.dht.SocialKademliaDHT;

/**
 * An operation that handles refreshing the entire Kademlia Systems including buckets and content
 *
 * @author Joshua Kissoon
 * @since 20140306
 */
public class KadRefreshOperation implements Operation
{

    private final KadServer server;
    private final SocialKademliaNode localNode;
    private final SocialKademliaDHT dht;
    private final KadConfiguration config;

    public KadRefreshOperation(KadServer server, SocialKademliaNode localNode, SocialKademliaDHT dht, KadConfiguration config)
    {
        this.server = server;
        this.localNode = localNode;
        this.dht = dht;
        this.config = config;
    }

    @Override
    public void execute() throws IOException
    {
        /* Run our BucketRefreshOperation to refresh buckets */
        new BucketRefreshOperation(this.server, this.localNode, this.config).execute();

        /* After buckets have been refreshed, we refresh content */
        new ContentRefreshOperation(this.server, this.localNode, this.dht, this.config).execute();
    }
}
