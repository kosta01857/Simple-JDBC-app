/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import rs.etf.sab.operations.VehicleOperations;
/**
 *
 * @author Zoki
 */
public class nk200640_MyVehicleOperations implements VehicleOperations{
     Database db;
    public nk200640_MyVehicleOperations(){
        db = Database.getInstance();
    }
    @Override
    public boolean insertVehicle(String string, int i, BigDecimal bd) {
        String sql = String.format("insert into vozilo (regbr,tip,potrosnja) values('%s',%s,%s)", string,i,bd.doubleValue());
        int res = db.post(sql);
        return res >= 1;
    }

    public boolean vehicleExists(String regBr){
	    String sql = String.format("select * from Vozilo where regBr='%s' ", regBr);
	    return !db.get(sql, 1).isEmpty();
    }
    public int getVehicleId(String regBr){
	    if(!vehicleExists(regBr)) return -1;
	    String sql = String.format("select * from Vozilo where regBr='%s'", regBr);
	    return Integer.parseInt((String)db.get(sql, 1).get(0));
    }
    public String getVehicleReg(int id){
	    String sql = String.format("select regBr from Vozilo where id=%s", id);
	    return (String)db.get(sql, 1).get(0);
    }

    @Override
    public int deleteVehicles(String... strings) {
        List<String> list = Arrays.asList(strings);
        AtomicInteger cnt = new AtomicInteger(0);
        list.forEach(regBr -> {
            cnt.addAndGet(db.post(String.format("DELETE FROM Vozilo WHERE regBr = '%s'", regBr)));
        });
        return cnt.get();
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> result = db.get("select * from Vozilo",4);
        return result;
    }

	@Override
	public boolean changeFuelType(String string, int i) {
		String sql = String.format("update vozilo set tip=%s where regBr='%s'", i, string);
		int res = db.post(sql);
		return res >= 1;
	}

	@Override
	public boolean changeConsumption(String string, BigDecimal bd) {
		String sql = String.format("update vozilo set potrosnja=%s where regBr='%s'", bd.doubleValue(), string);
		int res = db.post(sql);
		return res >= 1;
	}

	public static void main(String[] args) {
		nk200640_MyVehicleOperations op = new nk200640_MyVehicleOperations();
		System.out.println(op.insertVehicle("bg1111aa", 0, BigDecimal.valueOf(4.3)));
	}
}
