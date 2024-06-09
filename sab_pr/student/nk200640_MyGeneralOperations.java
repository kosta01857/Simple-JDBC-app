/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import rs.etf.sab.operations.*;

/**
 *
 * @author Zoki
 */
public class nk200640_MyGeneralOperations implements GeneralOperations{
    Database db;

    public nk200640_MyGeneralOperations(){
        db = Database.getInstance();
    }
    @Override
    public void eraseAll() {
        String sql = "DELETE FROM Ponuda";
        db.post(sql);
        sql = "DELETE FROM Paket";
        db.post(sql);
        sql = "DELETE FROM zahtevKurir";
        db.post(sql);
        sql = "DELETE FROM opstina";
        db.post(sql);
        sql = "DELETE FROM korisnik";
        db.post(sql);
        sql = "DELETE FROM vozilo";
        db.post(sql);
        sql = "DELETE FROM grad";
        db.post(sql);
    }

    public static void main(String[] args) {
        new nk200640_MyGeneralOperations().eraseAll();
    }
    
}
