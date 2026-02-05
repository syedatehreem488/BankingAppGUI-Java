import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String cnic;
    private String phoneNo;

    public Person() {}

    public Person(String name, String cnic, String phoneNo) {
        this.name = name;
        this.cnic = cnic;
        this.phoneNo = phoneNo;
    }

    public String getName() { return name; }
    public String getCnic() { return cnic; }
    public String getPhoneNo() { return phoneNo; }

    public void setName(String name) { this.name = name; }
    public void setCnic(String cnic) { this.cnic = cnic; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    @Override
    public String toString() {
        return "Person{name='" + name + "', cnic='" + cnic + "', phoneNo='" + phoneNo + "'}";
    }
}
