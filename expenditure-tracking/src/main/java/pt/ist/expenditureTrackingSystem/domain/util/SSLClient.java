package pt.ist.expenditureTrackingSystem.domain.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.fenixedu.bennu.core.rest.JsonBodyReaderWriter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;

/**
 * 
 * @author Ricardo Almeida
 *
 */
public class SSLClient {

    private final Client client;

    private static SSLClient sslClient;

    private SSLClient() {
        client = createClient();
    }

    public static SSLClient getInstance() {
        if (sslClient == null) {
            sslClient = new SSLClient();
        }
        return sslClient;
    }

    public Client getClient() {
        return client;
    }

    private static Client createClient() {
        if (ExpenditureConfiguration.get().checkSSLCertificates()) {
            return ClientBuilder.newBuilder().register(MultiPartFeature.class).register(JsonBodyReaderWriter.class).build();
        }
        final TrustManager trm = new javax.net.ssl.X509TrustManager() {
            @Override
            public final X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
            }
        };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { trm }, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e2) {
            e2.printStackTrace();
        }
        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).register(JsonBodyReaderWriter.class)
                .sslContext(sc).build();
        return client;
    }

}
