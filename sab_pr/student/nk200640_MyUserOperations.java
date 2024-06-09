/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.org.jline.terminal.TerminalBuilder;
import rs.etf.sab.operations.UserOperations;
/**
 *
 * @author Zoki
 */
public class nk200640_MyUserOperations implements UserOperations{
     Database db;
    public nk200640_MyUserOperations(){
        db = Database.getInstance();
    }
    @Override
    public boolean insertUser(String string, String string1, String string2, String string3) {
        String sql = String.format("insert into Korisnik (username,ime,prezime,password,brPaketa) values ('%s','%s','%s','%s',0)", string, string1,string2,string3);
        int res = db.post(sql);
        return res >= 1;
    }

    public boolean userExists(String username){
	    String sql = String.format("select * from Korisnik where username='%s'", username);
	    return !db.get(sql, 1).isEmpty();
    }
    @Override
    public int declareAdmin(String string) {
        String sql = String.format("select * from Korisnik where username='%s' and tip='A'", string);
        List res = db.get(sql, 1);
        if (res.size() > 0) {
            return 1;
        }
        sql = String.format("select * from Korisnik where username='%s'", string);
        res = db.get(sql, 1);
        if (res.isEmpty()) {
            return 2;
        }
        sql = String.format("update Korisnik set tip='A' where username='%s'", string);
        db.post(sql);
        return 0;
    }

    @Override
    public Integer getSentPackages(String... strings) {
        List<String> list = Arrays.asList(strings);
        AtomicInteger cnt = new AtomicInteger(0);
        try {
            list.forEach(name -> {
                cnt.addAndGet(Integer.parseInt((String) db.get(String.format("select brPaketa FROM Korisnik WHERE username = '%s'", name), 1).get(0)));
            });
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return cnt.get();
    }

    @Override
    public int deleteUsers(String... strings) {
        List<String> list = Arrays.asList(strings);
        AtomicInteger cnt = new AtomicInteger(0);
		list.forEach(name -> {
			cnt.addAndGet(db.post(String.format("DELETE FROM Korisnik WHERE username = '%s'", name)));
		});
		return cnt.get();
	}

	@Override
	public List<String> getAllUsers() {
		List<String> result = db.get("select * from Korisnik", 1);
		return result;
	}

	public static void main(String[] args) {
		nk200640_MyUserOperations op = new nk200640_MyUserOperations();
		System.out.println(op.insertUser("mile123", "Milan", "Djelic", "123"));

	}
}
