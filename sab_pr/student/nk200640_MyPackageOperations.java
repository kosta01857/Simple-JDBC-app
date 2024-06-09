/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author Zoki
 */
public class nk200640_MyPackageOperations implements PackageOperations {

	Database db;
	class MyPair implements Pair<Integer,BigDecimal>{
		Integer first;
		BigDecimal second;

		public MyPair(String col, String col1) {
			first = Integer.parseInt(col);
			second = new BigDecimal(col1);
		}

		@Override
		public Integer getFirstParam() {
			return first;
		}

		@Override
		public BigDecimal getSecondParam() {
			return second;
		}
	}
	public nk200640_MyPackageOperations() {
		db = Database.getInstance();
	}

	@Override
	public int insertPackage(int i, int i1, String string, int i2, BigDecimal bd) {
		String sql = String.format("insert into Paket (idSrc,idDst,idKor,tip,tezina,status) values(%s,%s,'%s',%s,%s,0)", i, i1, string, i2, bd.doubleValue());
		return db.postPK(sql);
	}

	@Override
	public int insertTransportOffer(String string, int i, BigDecimal bd) {
		String sql = String.format("select status from Korisnik where username='%s'", string);
		int status = Integer.parseInt((String)db.get(sql, 1).getFirst());
		if(status == 1) return -1;
		if (bd == null){
			double res = Math.random();
			int sign;  
			if(res > 0.5) sign = 1;
			else sign = -1;
			bd = new BigDecimal(Math.random()*11*sign);
		}
		sql = String.format("insert into Ponuda(idK,idP,procenat) values('%s',%s,%s)", string,i,bd.doubleValue());
		return db.postPK(sql);
	}

	@Override
	public boolean acceptAnOffer(int i) {
		String sql = String.format("select procenat from Ponuda where id=%s", i);
		float percent = Float.parseFloat((String)db.get(sql, 1).get(0));
		sql = String.format("select idP from Ponuda where id=%s", i);
		int idP= Integer.parseInt((String)db.get(sql, 1).get(0));
		sql = String.format("select idK from Ponuda where id=%s", i);
		String idK= (String)db.get(sql, 1).get(0);
		float price = db.execute(idP);
		price = price * (1+(percent/100));
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		sql = String.format("update Paket set cena=%s,idKurir='%s',vremePrihv='%s',status=1 where id=%s", price,idK,timestamp,idP);
		db.post(sql);
		sql = String.format("delete from Ponuda where id=%s", i);
		db.post(sql);
		return true;
	}

	@Override
	public List<Integer> getAllOffers() {
		String sql = "select * from Ponuda";
		List<String> res = db.get(sql, 4);
		List<Integer> ret = new ArrayList<>();
		res.forEach(id -> {
			ret.add(Integer.parseInt(id));
		});
		return ret;
	}

	@Override
	public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int i) {
		String sql = String.format("select * from Ponuda where idP=%s", i);
		List<String> results = (List<String>) db.get(sql,4,3);
		List<Pair<Integer, BigDecimal>> ret = new ArrayList<>();
		results.forEach(result -> {
			String[] cols = result.split("&");
			Pair<Integer,BigDecimal> pair = new MyPair(cols[0],cols[1]);
			ret.add(pair);
		});
		return ret;
	}

	@Override
	public boolean deletePackage(int i) {
		String sql = String.format("delete from Paket where id=%s",i);
		return db.post(sql) > 0;
	}

	@Override
	public boolean changeWeight(int i, BigDecimal bd) {
		String sql = String.format("update Paket set tezina=%s where id=%s",bd.doubleValue(),i);
		return db.post(sql) > 0;
	}

	@Override
	public boolean changeType(int i, int i1) {
		String sql = String.format("update Paket set tip=%s where id=%s",i1,i);
		return db.post(sql) > 0;
	}


	@Override
	public Integer getDeliveryStatus(int i) {
		String sql = String.format("select status from Paket where id=%s", i);
		Integer res = Integer.parseInt((String)db.get(sql, 1).get(0));
		return res;
	}

	@Override
	public BigDecimal getPriceOfDelivery(int i) {
		String sql = String.format("select cena from Paket where id=%s", i);
		float res = Float.parseFloat((String)db.get(sql, 1).get(0));
		return BigDecimal.valueOf(res);
	}


	@Override
	public Date getAcceptanceTime(int i) {
		String sql = String.format("select vremePrihv from Paket where id=%s",i);	
		String res = (String)db.get(sql, 1).get(0);
		Timestamp timestamp = java.sql.Timestamp.valueOf(res);
		return new java.sql.Date(timestamp.getTime());
	}

	@Override
	public List<Integer> getAllPackagesWithSpecificType(int i) {
		String sql = String.format("select * from Paket where tip=%s",i);
		List<String> res = db.get(sql, 1);
		List<Integer> ret = new ArrayList<>();
		res.forEach(id -> {
			ret.add(Integer.parseInt(id));
		});
		return ret;
	}

	@Override
	public List<Integer> getAllPackages() {
		String sql = "select * from Paket";
		List<String> res = db.get(sql, 1);
		List<Integer> ret = new ArrayList<>();
		res.forEach(id -> {
			ret.add(Integer.parseInt(id));
		});
		return ret;
	}

	@Override
	public List<Integer> getDrive(String string) {
		String sql = String.format("select * from Paket where idKurir='%s' and (status=1 or status=2) order by vremePrihv asc", string);
		List<String> res = db.get(sql, 1);
		List<Integer> ret = new ArrayList<>();
		res.forEach(id -> {
			ret.add(Integer.parseInt(id));
		});
		return ret;
	}
	float distance(float x1,float x2,float y1,float y2){
		return (float)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
	}
	int getGasPrice(int tip){
		int gas_price;
		switch (tip){
			case 0:{
				gas_price = 15;
				break;
			}
			case 1:{
				gas_price = 32;
				break;
			}
			case 2:{
				gas_price = 36;
			}
			default:gas_price = 32;
		}return gas_price;
	}
	static class Context{
		float total_profit = 0;
			float total_loss = 0;
			float xS = -1;
			float yS = -1;
			float xD = -1;
			float yD = -1;	
	}
	public float getProfit(String username){
		Context lambda_context = new Context();
		
		float profit;
		float potrosnja;
		int tip;
		String sql = String.format("select idV from Korisnik where username='%s'",username);
		int idV = Integer.parseInt((String)db.get(sql,1).getFirst());
		sql = String.format("select tip,potrosnja from Vozilo where id=%s",idV);
		String query_res = (String)db.get(sql,1,2).getFirst();
		String[] vehicle_details = query_res.split("&");
		tip = Integer.parseInt(vehicle_details[0]);
		int gas_price = getGasPrice(tip);
		potrosnja = Float.parseFloat(vehicle_details[1]);
		sql = String.format("select idSrc,idDst,cena from Paket where idKurir='%s' and status=3 order by vremePrihv asc", username);
		List<String> results = db.get(sql, 1,2,3);
		results.forEach(result -> {

			String[] cols = result.split("&");
			int idSrc = Integer.parseInt(cols[0]);
			int idDst = Integer.parseInt(cols[1]);
			float price = Float.parseFloat(cols[2]);
			String lambda_sql = String.format("select x,y from Opstina where id=%s",idSrc);
			String res = (String)db.get(lambda_sql,1,2).getFirst();
			String[] coords = res.split("&");
			lambda_context.xS = Float.parseFloat(coords[0]);
			lambda_context.yS = Float.parseFloat(coords[1]);
			if(lambda_context.xD != -1){
				float distance = distance(lambda_context.xD,lambda_context.xS,lambda_context.yD,lambda_context.yS);
				lambda_context.total_loss += distance*potrosnja*gas_price;
			}
			lambda_sql = String.format("select x,y from Opstina where id=%s",idDst);
			res = (String)db.get(lambda_sql,1,2).getFirst();
			coords = res.split("&");
			lambda_context.xD = Float.parseFloat(coords[0]);
			lambda_context.yD = Float.parseFloat(coords[1]);
			float distance = distance(lambda_context.xD,lambda_context.xS,lambda_context.yD,lambda_context.yS);
			lambda_context.total_profit += price;
			lambda_context.total_loss += distance*potrosnja*gas_price;
		});
		return lambda_context.total_profit - lambda_context.total_loss;
	}
	@Override
	public int driveNextPackage(String string) {
		List<Integer> packages = getDrive(string);
		if(packages.isEmpty()) {
			return -1;
		}
		String sql = String.format("select * from Korisnik where username='%s' and tip='K'", string);
		int status = Integer.parseInt((String)db.get(sql, 6).getFirst());
		if(status == 0) {
			sql = String.format("update Korisnik set status=1 where username='%s'", string);
			db.post(sql);
		}
		int idP = packages.removeFirst();
		sql = String.format("update Paket set status=3 where id=%s", idP);
		int ret = db.post(sql);
		if(!packages.isEmpty()){
			int idP2 = packages.removeFirst();
			sql = String.format("update Paket set status=2 where id=%s", idP2);
			db.post(sql);
		}
		else{
			float profit = getProfit(string);
			sql = String.format("update Korisnik set profit=profit+%s,status=0 where username='%s'",profit,string);
			db.post(sql);
		}
		return idP;
	}

	public static void main(String[] args) {
		//System.out.println(new MyPackageOperations().insertPackage(1124,1123,"mile123",1,BigDecimal.valueOf(12)));

		//System.out.println(new MyPackageOperations().insertTransportOffer("mile123",11,BigDecimal.valueOf(10)));
		//System.out.println(new MyPackageOperations().acceptAnOffer(6));
		//System.out.println(new MyPackageOperations().getPriceOfDelivery(4));
		System.out.println(new nk200640_MyPackageOperations().getAllOffersForPackage(11));

	}

}
