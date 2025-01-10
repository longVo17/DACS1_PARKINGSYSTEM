package function;

import Model.Vehicle;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FeeCalculator {
    private static final double CAR_DAY_FEE = 20000.0;
    private static final double MOTO_DAY_FEE = 5000.0;
    private static final double BICYCLE_DAY_FEE = 2000.0;
    private static final double CAR_MONTH_FEE = 400000.0;
    private static final double MOTO_MONTH_FEE = 50000.0;
    private static final double BICYCLE_MONTH_FEE = 25000.0;

    public static String calculateFeeTotal(Vehicle vehicle) {
        String ticketType = vehicle.getId_ticket().substring(0, 2);
        String vehicleType = vehicle.getVehicleName();
        double fee = 0;
        long penalty = 0;

        if (vehicle.getDateReturn().getTime() > vehicle.getDateExpired().getTime()) {
            long diffInMillies = Math.abs(vehicle.getDateReturn().getTime() - vehicle.getDateExpired().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (ticketType.equals("DT")) {
                switch (vehicleType) {
                    case "Car":
                        fee = CAR_DAY_FEE;
                        penalty = (long) (diff * 2 * CAR_DAY_FEE);
                        break;
                    case "Motorbike":
                        fee = MOTO_DAY_FEE;
                        penalty = (long) (diff * 2 * MOTO_DAY_FEE);
                        break;
                    case "Bicycle":
                        fee = BICYCLE_DAY_FEE;
                        penalty = (long) (diff * 2 * BICYCLE_DAY_FEE);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
                }
            } else if (ticketType.equals("MT")) {
                switch (vehicleType) {
                    case "Car":
                        fee = CAR_MONTH_FEE;
                        penalty = (long) ((diff / 30) * 2 * CAR_MONTH_FEE);
                        break;
                    case "Motorbike":
                        fee = MOTO_MONTH_FEE;
                        penalty = (long) ((diff / 30) * 2 * MOTO_MONTH_FEE);
                        break;
                    case "Bicycle":
                        fee = BICYCLE_MONTH_FEE;
                        penalty = (long) ((diff / 30) * 2 * BICYCLE_MONTH_FEE);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
                }
            } else {
                throw new IllegalArgumentException("Unknown ticket type: " + ticketType);
            }
        }
        else {
            // If there is no difference between return date and expiry date, calculate fee without penalty
            switch (vehicleType) {
                case "Car":
                    fee = ticketType.equals("DT") ? CAR_DAY_FEE : CAR_MONTH_FEE;
                    break;
                case "Motorbike":
                    fee = ticketType.equals("DT") ? MOTO_DAY_FEE : MOTO_MONTH_FEE;
                    break;
                case "Bicycle":
                    fee = ticketType.equals("DT") ? BICYCLE_DAY_FEE : BICYCLE_MONTH_FEE;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
            }
        }

        fee += penalty;

        NumberFormat vietnam = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return vietnam.format(fee);
    }

    public static String calculatePenalty(Vehicle vehicle) {
        String ticketType = vehicle.getId_ticket().substring(0, 2);
        String vehicleType = vehicle.getVehicleName();
        double fee = 0;
        long penalty = 0;

        if (vehicle.getDateReturn().getTime() > vehicle.getDateExpired().getTime()) {
            long diffInMillies = Math.abs(vehicle.getDateReturn().getTime() - vehicle.getDateExpired().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (ticketType.equals("DT")) {
                switch (vehicleType) {
                    case "Car":
                        fee = CAR_DAY_FEE;
                        penalty = (long) (diff * 2 * CAR_DAY_FEE);
                        break;
                    case "Motorbike":
                        fee = MOTO_DAY_FEE;
                        penalty = (long) (diff * 2 * MOTO_DAY_FEE);
                        break;
                    case "Bicycle":
                        fee = BICYCLE_DAY_FEE;
                        penalty = (long) (diff * 2 * BICYCLE_DAY_FEE);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
                }
            } else if (ticketType.equals("MT")) {
                switch (vehicleType) {
                    case "Car":
                        fee = CAR_MONTH_FEE;
                        penalty = (long) ((diff / 30) * 2 * CAR_MONTH_FEE);
                        break;
                    case "Motorbike":
                        fee = MOTO_MONTH_FEE;
                        penalty = (long) ((diff / 30) * 2 * MOTO_MONTH_FEE);
                        break;
                    case "Bicycle":
                        fee = BICYCLE_MONTH_FEE;
                        penalty = (long) ((diff / 30) * 2 * BICYCLE_MONTH_FEE);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
                }
            } else {
                throw new IllegalArgumentException("Unknown ticket type: " + ticketType);
            }
        }

        fee += penalty;

        NumberFormat vietnam = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return vietnam.format(penalty);
    }

    public static String calculateFee(Vehicle vehicle) {
        String ticketType = vehicle.getId_ticket().substring(0, 2);
        String vehicleType = vehicle.getVehicleName();
        double fee = 0;

            if (ticketType.equals("DT")) {
                switch (vehicleType) {
                    case "Car":
                        fee = CAR_DAY_FEE;
                        break;
                    case "Motorbike":
                        fee = MOTO_DAY_FEE;
                        break;
                    case "Bicycle":
                        fee = BICYCLE_DAY_FEE;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
                }
            } else if (ticketType.equals("MT")) {
                switch (vehicleType) {
                    case "Car":
                        fee = CAR_MONTH_FEE;
                        break;
                    case "Motorbike":
                        fee = MOTO_MONTH_FEE;
                        break;
                    case "Bicycle":
                        fee = BICYCLE_MONTH_FEE;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
                }
            } else {
                throw new IllegalArgumentException("Unknown ticket type: " + ticketType);
            }
        NumberFormat vietnam = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return vietnam.format(fee);
    }
}