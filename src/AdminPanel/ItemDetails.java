package AdminPanel;

import java.util.HashMap;

public class ItemDetails {

    public void checkBoxesAmount(String checkbox, HashMap<String, Double> checkBoxAmounts){
        if (checkbox.equals("Stereo System")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Paint")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("GPS")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Leather Coating")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Rims")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Tyres")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Exhaust")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Alignment")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Oil Change")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Battery Replacement")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Brake repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Catalytic converter")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Transmission repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Wiring System")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Thermostat replacement")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("A/C repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Timing belt")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Ignition system")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Dent repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Panel replacement")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Fluid checks")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Air Filters")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Climate control")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else if (checkbox.equals("Engine repair")) {
            checkBoxAmounts.put(checkbox, 100.0);
        } else {
            checkBoxAmounts.put(checkbox, 10.0);
        }
    }
}
