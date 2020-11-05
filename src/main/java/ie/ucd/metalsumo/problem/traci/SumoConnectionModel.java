/**
 *
 * @author Javier Morales (jmorales@iiia.csic.es)
 */
package ie.ucd.metalsumo.problem.traci;

import java.util.ArrayList;

/**
 * @author HP
 *
 */
public class SumoConnectionModel {
    
    protected final String config_file = "src/traci/config.sumocfg";

    public SumoConnectionModel() {
    }
    
    

    public void run(double[] arr) {
        //starting sumo and sending data to it
        ArrayDataUsage sumoArray = new ArrayDataUsage(config_file, arr, arr.length);
        //getting data from sumo
        ArrayList<String> carsDetails = sumoArray.gettingCarsDetails();
        for (String car : carsDetails) {
            System.out.println(car);
        }
    }

}
