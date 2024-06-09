/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.util.List;
import rs.etf.sab.operations.CourierOperations;
/**
 *
 * @author Zoki
 */
public class nk200640_MyCourierOperations implements CourierOperations {
     Database db;
    public nk200640_MyCourierOperations(){
        db = Database.getInstance();
    }
    @Override
    public boolean insertCourier(String string, String string1) {
	    int id = new nk200640_MyVehicleOperations().getVehicleId(string1);
	    String sql = String.format("update Korisnik set tip = 'K',idV=%s ,profit=0,status=0 where username='%s'",id,string);
	    int ret = db.post(sql);
	    return ret >= 1;
    }
    public boolean courierExists(String username){
	    String sql = String.format("select * from Korisnik where username='%s' and tip='K'", username);
	    return !db.get(sql, 1).isEmpty();
    }
    @Override
    public boolean deleteCourier(String string) {
	    if(!courierExists(string)) return false;
	    String sql = String.format("delete from Korisnik where username='%s'", string);
	    return db.post(sql) > 0;
    }

	@Override
	public List<String> getCouriersWithStatus(int i) {
		String sql = String.format("select * from Korisnik where status=%s and tip='K'", i);
		return db.get(sql, 1);
	}

	@Override
	public List<String> getAllCouriers() {
		String sql ="select username from Korisnik where tip='K'";
		return (List<String>)db.get(sql, 1);
	}

	@Override
	public BigDecimal getAverageCourierProfit(int i) {
		String sql = String.format("select avg(profit) from Korisnik where tip='K' and brPaketa>=%s", i);
		return new BigDecimal((String)db.get(sql, 1).getFirst());
	}
	public static void main(String[] args) {
		System.out.println(new nk200640_MyCourierOperations().deleteCourier("mile123"));

	}
}
