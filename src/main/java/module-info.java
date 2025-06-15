module net.alex9849.motorlib.motorlib {
    requires com.pi4j;
    requires com.pi4j.plugin.raspberrypi;
    requires com.pi4j.plugin.pigpio;
    requires com.pi4j.library.pigpio;
    requires com.pi4j.plugin.linuxfs;

    uses com.pi4j.extension.Extension;
    uses com.pi4j.provider.Provider;

    // allow access to classes in the following namespaces for Pi4J annotation processing
    opens net.alex9849.motorlib to com.pi4j;

    //exports motorlib;
}