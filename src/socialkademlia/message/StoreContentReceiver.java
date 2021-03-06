package socialkademlia.message;

import java.io.IOException;
import kademlia.KadServer;
import kademlia.message.Message;
import kademlia.message.Receiver;
import socialkademlia.SocialKademliaNode;
import socialkademlia.dht.JSocialKademliaStorageEntry;
import socialkademlia.dht.SocialKademliaDHT;
import socialkademlia.dht.SocialKademliaStorageEntry;

/**
 * Receiver for incoming StoreContentMessage
 *
 * @author Joshua Kissoon
 * @since 20140225
 */
public class StoreContentReceiver implements Receiver
{

    private final KadServer server;
    private final SocialKademliaNode localNode;
    private final SocialKademliaDHT dht;

    public StoreContentReceiver(KadServer server, SocialKademliaNode localNode, SocialKademliaDHT dht)
    {
        this.server = server;
        this.localNode = localNode;
        this.dht = dht;
    }

    @Override
    public void receive(Message incoming, int comm)
    {
        /* It's a StoreContentMessage we're receiving */
        StoreContentMessage msg = (StoreContentMessage) incoming;

        /* Insert the message sender into this node's routing table */
        this.localNode.getRoutingTable().insert(msg.getOrigin());

        try
        {
            /**
             * Store this Content into the DHT
             *
             * Specify that this content should not be cached
             *
             * Specify that this node is one of the k-closest to the content
             */
            JSocialKademliaStorageEntry entry = msg.getContent();
            entry.getContentMetadata().setCached(false);
            entry.getContentMetadata().setKNode();
            this.dht.store(entry);
        }
        catch (IOException e)
        {
            System.err.println("Unable to store received content; Message: " + e.getMessage());
        }

    }

    @Override
    public void timeout(int comm)
    {
        /**
         * This receiver only handles Receiving content when we've received the message,
         * so no timeout will happen with this receiver.
         */
    }
}
