/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author Zoki
 */
public class nk200640_MyDistrictOperations implements DistrictOperations {
	
	Database db;
	
	public nk200640_MyDistrictOperations() {
		db = Database.getInstance();
	}
	
	@Override
	public int insertDistrict(String string, int i, int i1, int i2) {
		String sql = String.format("insert into Opstina(naziv,idGrad,x,y) values ('%s',%s,%s,%s)", string, i,i1,i2);
		return db.postPK(sql);
	}
	
	@Override
	public int deleteDistricts(String... strings) {
		List list = Arrays.asList(strings);
		AtomicInteger cnt = new AtomicInteger(0);
		list.forEach(naziv -> {
			String sql = String.format("delete from Opstina where naziv='%s'", naziv);
			cnt.addAndGet(db.post(sql));
		});
		return cnt.get();
	}
	
	@Override
	public boolean deleteDistrict(int i) {
		String sql = String.format("delete from Opstina where id=%s", i);
		int res = db.post(sql);
		return res > 0;
	}
	
	@Override
	public int deleteAllDistrictsFromCity(String string) {
		int id = new nk200640_MyCityOperations().getCityFromName(string);
		if(id == -1) return 0;
		String sql = String.format("delete from Opstina where idGrad=%s",id);
		int res = db.post(sql);
		return res;
		
	}
	
	@Override
	public List<Integer> getAllDistrictsFromCity(int i) {
		String sql = String.format("select * from Opstina where idGrad=%s",i);
		List<String> res = db.get(sql, 5);
		List<Integer> ret = new ArrayList<>();
		res.forEach((element) -> ret.add(Integer.parseInt(element)));
		return ret;
	}

	@Override
	public List<Integer> getAllDistricts() {
		List<String> res = db.get("select * from Opstina", 5);
		List<Integer> ret = new ArrayList<>();
		res.forEach((element) -> ret.add(Integer.parseInt(element)));
		return ret;
	}

	public static void main(String[] args) {
		System.out.println(new nk200640_MyDistrictOperations().insertDistrict("centar2", 1273, 100, 100));
	}

}
