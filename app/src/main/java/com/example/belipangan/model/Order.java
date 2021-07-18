package com.example.belipangan.model;

import java.io.Serializable;

public class Order implements Serializable {
    String uidBuyer, namaProduct, namaPembeli, alamatTujuan, status, idOrder, uidSeller, idProduk;
    int kuantitas, totalHarga;

    public Order(String uidBuyer, String namaProduct, String namaPembeli, String alamatTujuan,
                 String status, int kuantitas, int totalHarga, String idOrder, String uidSeller,
                 String idProduk) {
        this.uidBuyer = uidBuyer;
        this.namaProduct = namaProduct;
        this.namaPembeli = namaPembeli;
        this.alamatTujuan = alamatTujuan;
        this.status = status;
        this.kuantitas = kuantitas;
        this.totalHarga = totalHarga;
        this.idOrder = idOrder;
        this.uidSeller = uidSeller;
        this.idProduk = idProduk;
    }

    public Order() {
    }

    public String getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(String idProduk) {
        this.idProduk = idProduk;
    }

    public String getUidSeller() {
        return uidSeller;
    }

    public void setUidSeller(String uidSeller) {
        this.uidSeller = uidSeller;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getUidBuyer() {
        return uidBuyer;
    }

    public void setUidBuyer(String uidBuyer) {
        this.uidBuyer = uidBuyer;
    }

    public String getNamaProduct() {
        return namaProduct;
    }

    public void setNamaProduct(String namaProduct) {
        this.namaProduct = namaProduct;
    }

    public String getNamaPembeli() {
        return namaPembeli;
    }

    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
    }

    public String getAlamatTujuan() {
        return alamatTujuan;
    }

    public void setAlamatTujuan(String alamatTujuan) {
        this.alamatTujuan = alamatTujuan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }
}
