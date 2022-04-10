package com.epam.finalproject.repository;

import com.epam.finalproject.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    public void deleteByInvoiceCode(Long invoiceCode);
    public void findByInvoiceId(Long id);
    public Page<Invoice> findAll(Pageable pageable);

}
