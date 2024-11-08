import com.tailf.jnc.*;

public class NETCONFConfig {
    private static final String DEVICE_HOST = "127.0.0.1"; // Replace with device IP
    private static final int NETCONF_PORT = 830;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    public static void main(String[] args) throws Exception {
        Device device = new Device("Router", DEVICE_HOST, NETCONF_PORT, USERNAME, PASSWORD);
        device.connect(USERNAME, PASSWORD);

        // Configure VLAN 100
        String vlanConfig = "<vlan><vlan-id>100</vlan-id><name>Test VLAN</name></vlan>";
        device.getSession().editConfig(vlanConfig, Datastore.running);

        // Verify VLAN
        String vlanData = device.getSession().getConfig(Datastore.running, "<vlan><vlan-id>100</vlan-id></vlan>");
        System.out.println("VLAN Configured: " + vlanData);

        // Wait for 5 minutes and delete VLAN 100
        Thread.sleep(300000);
        String deleteVlan = "<vlan><vlan-id>100</vlan-id></vlan>";
        device.getSession().editConfig(deleteVlan, Datastore.running, EditConfig.DEFAULT_OPERATION_REMOVE);
        System.out.println("VLAN 100 removed");

        device.close();
    }
}
