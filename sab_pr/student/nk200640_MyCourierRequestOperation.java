/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;
import java.util.List;
import rs.etf.sab.operations.CourierRequestOperation;
/**
 *
 * @author Zoki
 */
public class nk200640_MyCourierRequestOperation implements CourierRequestOperation{
     Database db;
    public nk200640_MyCourierRequestOperation(){
        db = Database.getInstance();
    }
    @Override
    public boolean insertCourierRequest(String string, String string1) {
	if(!(new nk200640_MyUserOperations().userExists(string))) return false;
	if(!(new nk200640_MyVehicleOperations().vehicleExists(string1))) return false;
	if((new nk200640_MyCourierOperations().courierExists(string))) return false;
	int id = new nk200640_MyVehicleOperations().getVehicleId(string1);
	String sql = String.format("insert into zahtevKurir (username,idV) values('%s',%s)", string,id);
	return db.post(sql) > 0;
    }

    @Override
    public boolean deleteCourierRequest(String string) {
	    String sql = String.format("delete from zahtevKurir where username='%s'", string);
	    return db.post(sql) > 0;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String string, String string1) {
	if(!(new nk200640_MyUserOperations().userExists(string))) return false;
	if(!(new nk200640_MyVehicleOperations().vehicleExists(string1))) return false;
	if((new nk200640_MyCourierOperations().courierExists(string))) return false;
	int id = new nk200640_MyVehicleOperations().getVehicleId(string1);
	String sql = String.format("update zahtevKurir set idV=%s where username='%s' ",id,string);
	return db.post(sql) > 0;
    }

    @Override
    public List<String> getAllCourierRequests() {
		String sql = "select username from zahtevKurir";
		return (List<String>) db.get(sql, 1);

	}

	public boolean requestExists(String username) {
		String sql = String.format("select * from zahtevKurir where username='%s' ", username);
		return !db.get(sql, 1).isEmpty();
	}

	@Override
	public boolean grantRequest(String string) {
		if(!(new nk200640_MyCourierRequestOperation().requestExists(string))) return false;
		String sql = String.format("select idV from zahtevKurir where username='%s'", string);
		int id = Integer.parseInt((String) db.get(sql, 1).get(0));
		String regBr = (new nk200640_MyVehicleOperations().getVehicleReg(id));
		boolean success = (new nk200640_MyCourierOperations().insertCourier(string, regBr));
		if(success){
			sql = String.format("delete from zahtevKurir where username='%s'", string);
			db.post(sql);
		}
		return success;
	}

	public static void main(String[] args) {
		System.out.println(new nk200640_MyCourierRequestOperation().grantRequest("mile123"));
	}

}
