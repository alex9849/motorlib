/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.alex9849.motorlib;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.plugin.linuxfs.provider.i2c.LinuxFsI2CProvider;
import net.alex9849.motorlib.xl9535.XL9535;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Context pi4J = Pi4J.newContextBuilder()
                .add(LinuxFsI2CProvider.newInstance())
                .build();
        I2CProvider i2CProvider = pi4J.provider("linuxfs-i2c");
        I2CConfig i2CConfig = I2C.newConfigBuilder(pi4J).bus(1).device(0x20).build();
        I2C i2c = i2CProvider.create(i2CConfig);
        XL9535 xl9535 = new XL9535(i2c);
        xl9535.writeAll(true);
        Thread.sleep(2000);
        xl9535.writeAll(false);
    }

}
