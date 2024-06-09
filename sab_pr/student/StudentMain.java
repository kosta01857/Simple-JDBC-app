package student;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;
import rs.etf.sab.operations.*;

public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new nk200640_MyCityOperations();
        DistrictOperations districtOperations = new nk200640_MyDistrictOperations();
        CourierOperations courierOperations = new nk200640_MyCourierOperations();
        CourierRequestOperation courierRequestOperation = new nk200640_MyCourierRequestOperation();
        GeneralOperations generalOperations = new nk200640_MyGeneralOperations();
        UserOperations userOperations = new nk200640_MyUserOperations();
        VehicleOperations vehicleOperations = new nk200640_MyVehicleOperations();
        PackageOperations packageOperations = new nk200640_MyPackageOperations();
        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    }
}
