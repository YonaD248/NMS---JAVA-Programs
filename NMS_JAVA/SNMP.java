import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ResponseEvent;
import org.snmp4j.Snmp;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.mp.SnmpConstants;



public class SNMPMonitor {
    private static final String SNMP_COMMUNITY = "public";
    private static final String SNMP_HOST = "127.0.0.1"; // Replace with your router IP
    private static final int SNMP_PORT = 161;

    public static void main(String[] args) {
        try {
            Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();

            // Retrieve hostname and OS version
            String hostnameOID = "1.3.6.1.2.1.1.5.0"; // Replace with actual OID for hostname
            String osVersionOID = "1.3.6.1.2.1.1.1.0"; // Replace with actual OID for OS version

            System.out.println("Hostname: " + getSNMPData(snmp, hostnameOID));
            System.out.println("OS Version: " + getSNMPData(snmp, osVersionOID));

            // Monitor interface statuses
            String interfaceOID = "1.3.6.1.2.1.2.2.1.8.1"; // Replace with actual OID for interface status
            String interfaceStatus = getSNMPData(snmp, interfaceOID);
            if ("down".equals(interfaceStatus)) {
                System.out.println("Alert: Interface is down");
            }
            snmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getSNMPData(Snmp snmp, String oid) throws Exception {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(SNMP_COMMUNITY));
        target.setAddress(new UdpAddress(SNMP_HOST + "/" + SNMP_PORT));
        target.setVersion(SnmpConstants.version2c);

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);

        ResponseEvent response = snmp.get(pdu, target);
        if (response.getResponse() != null) {
            return response.getResponse().get(0).getVariable().toString();
        } else {
            throw new Exception("No response from SNMP agent.");
        }
    }
}
