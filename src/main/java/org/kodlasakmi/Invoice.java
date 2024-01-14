package org.kodlasakmi;
import java.util.ArrayList;
import java.util.List;

class Invoice {

    private String invoice_number, type, account, name, job_num, cashier_user, sis_mat_br, date, total, cost, gm_perc;

    private List<String[]> items = new ArrayList<>();

    public String getInvoice_number() { return invoice_number; }

    public String getType() { return type; }

    public String getAccount() { return account; }

    public String getName() { return name; }

    public String getJob_num() { return job_num; }

    public String getCashier_user() { return cashier_user; }

    public String getSis_mat_br() { return sis_mat_br; }

    public String getDate() { return date; }

    public String getTotal() { return total; }

    public String getCost() { return cost; }

    public String getGm_perc() { return gm_perc; }

    public List<String[]> getItems() { return items; }

    public void setInvoice_number(String invoice_number) {  this.invoice_number = invoice_number; }

    public void setType(String type) { this.type = type; }

    public void setAccount(String account) { this.account = account; }

    public void setName(String name) { this.name = name; }

    public void setJob_num(String job_num) { this.job_num = job_num; }

    public void setCashier_user(String cashier_user) { this.cashier_user = cashier_user; }

    public void setSis_mat_br(String sis_mat_br) { this.sis_mat_br = sis_mat_br; }

    public void setDate(String date) { this.date = date; }

    public void setTotal(String total) { this.total = total; }

    public void setCost(String cost) { this.cost = cost; }

    public void setGm_perc(String gm_perc) { this.gm_perc = gm_perc; }

    public void setItems(List<String[]> items) { this.items = items; }

    @Override
    public String toString() {

        String part_nums = "";

        for ( String[] item : items ) {
            part_nums = part_nums + item[0] + " :: ";
        }

        return "Invoice{" +
                "invoice_number='" + invoice_number + '\'' +
                ", type='" + type + '\'' +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", job_num='" + job_num + '\'' +
                ", cashier_user='" + cashier_user + '\'' +
                ", sis_mat_br='" + sis_mat_br + '\'' +
                ", date='" + date + '\'' +
                ", total='" + total + '\'' +
                ", cost='" + cost + '\'' +
                ", gm_perc='" + gm_perc + '\'' +
                ", items=" + part_nums +
                '}';
    }
}