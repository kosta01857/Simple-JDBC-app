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
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author Zoki
 */
public class nk200640_MyCityOperations implements CityOperations {
    Database db;
    public nk200640_MyCityOperations() {
        db = Database.getInstance();
    }

    @Override
    public int insertCity(String string, String string1) {
        if (getCityFromPostal(string1)!=-1) return -1; //vec postoji
        if (getCityFromName(string)!=-1) return -1; //vec postoji
        String sql = String.format("insert into grad (naziv,postBr) values ('%s',%s)", string, string1);
        return db.postPK(sql);

    }
    public int getCityFromName(String string){
	    String sql = String.format("select id from Grad where naziv='%s'",string);
	    List<String> res = db.get(sql, 1);
	    if(res.isEmpty()) return -1;
	    return Integer.parseInt((String)res.getFirst());
    }
    public int getCityFromPostal(String string){
        String sql = String.format("select id from Grad where postBr=%s",string);
        List<String> res = db.get(sql, 1);
        if(res.isEmpty()) return -1;
        return Integer.parseInt((String)res.getFirst());
    }

    @Override
    public int deleteCity(String... strings) {
        List<String> list = Arrays.asList(strings);
        AtomicInteger cnt = new AtomicInteger(0);
        list.forEach(name -> {
            cnt.addAndGet(db.post(String.format("DELETE FROM grad WHERE naziv = '%s'", name)));
        });
        return cnt.get();
    }

    @Override
    public boolean deleteCity(int i) {
        int res = db.post(String.format("DELETE FROM grad WHERE id = %s", Integer.toString(i)));
	return res >= 1;
    }

    @Override
    public List<Integer> getAllCities() {
        List<String> res = db.get("select * from Grad", 1);
        List<Integer> ret = new ArrayList<>();
        res.forEach((element) -> ret.add(Integer.parseInt(element)));
        return ret;
    }

    public static void main(String[] args) {
        nk200640_MyCityOperations op = new nk200640_MyCityOperations();
        System.out.println(op.insertCity("Krusevac" ,"50000" ));
    }

}
