package com.epam.finalproject.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "invoice_code")
    private Long invoiceCode;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Order> orders;


    @ElementCollection(targetClass = InvoiceStatus.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "invoice_status", joinColumns = @JoinColumn(name = "invoice_id"))
    @Enumerated(EnumType.STRING)
    private Set<InvoiceStatus> status;

    @Column(name = "invoice_date")
    private Date date;

    @Column(name = "invoice_notes")
    private String invoiceNotes;

    public void setOrders(List<Order> orders) {
        if (orders != null){
            orders.forEach(o->{
                o.setInvoice(this);
            });
        }

        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        if (invoiceId != null ? !invoiceId.equals(invoice.invoiceId) : invoice.invoiceId != null) return false;
        return invoiceCode.equals(invoice.invoiceCode);
    }

    @Override
    public int hashCode() {
        int result = invoiceId != null ? invoiceId.hashCode() : 0;
        result = 31 * result + invoiceCode.hashCode();
        return result;
    }

}
