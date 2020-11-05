/**
 *
 * @author Javier Morales (jmorales@iiia.csic.es)
 */
package ie.ucd.metalsumo.problem.traci;

import java.util.ArrayList;
import de.tudresden.sumo.cmd.Inductionloop;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoVehicleData;
import it.polito.appeal.traci.SumoTraciConnection;

/**
 * @author HP
 *
 */
public class ArrayDataUsage {

    private double[] MaxSpeed;
    private SumoTraciConnection conn;
    private ArrayList<String> vehicleData;

    public ArrayDataUsage(String configuration_file, double[] arr, int no_of_timeSteps) {

        this.MaxSpeed = arr;

        vehicleData = new ArrayList<String>();
        //Starting SUMO
        if (SUMOstart(configuration_file) == true) {
            //Vehicles Creation
            vehiclesCreation(no_of_timeSteps);

        }

    }

    public boolean SUMOstart(String configuration_file) {

        String sumo_bin = "sumo-gui";
        String config_file = configuration_file;
        double step_length = 0.1;

        try {
            conn = new SumoTraciConnection(sumo_bin, config_file);
            conn.addOption("step-length", step_length + "");
            conn.addOption("start", "true"); //start sumo immediately

            //start Traci Server
            conn.runServer();
            conn.setOrder(1);

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void vehiclesCreation(int no_of_timeSteps) {
        try {
            for (int i = 0; i < no_of_timeSteps; i++) {
                conn.do_timestep();
                conn.do_job_set(Vehicle.addFull("v" + i, "r1", "car", "now", "0", "0", "max", "current", "max", "current", "", "", "", 0, 0));

                conn.do_job_set(Vehicle.setMaxSpeed("v" + i, MaxSpeed[i]));

                SumoVehicleData vehData = (SumoVehicleData) conn.do_job_get(Inductionloop.getVehicleData("loop1"));

                for (SumoVehicleData.VehicleData d : vehData.ll) {

                    vehicleData.add(String.format("  veh=%s len=%s entry=%s leave=%s type=%s", d.vehID, d.length, d.entry_time, d.leave_time, d.typeID));

                }

                /*SumoStringList l=(SumoStringList) conn.do_job_get(Inductionloop.getIDList());
             	for (String s:l)
             	{
              System.out.println(s);
             	}*/
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public ArrayList<String> gettingCarsDetails() {
        return vehicleData;
    }

}
