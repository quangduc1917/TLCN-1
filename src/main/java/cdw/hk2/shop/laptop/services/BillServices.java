package cdw.hk2.shop.laptop.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cdw.hk2.shop.laptop.model.Bill;
import cdw.hk2.shop.laptop.model.Review;
import cdw.hk2.shop.laptop.repository.IBillRespository;
@Service
public class BillServices {
@Autowired
private IBillRespository iBillRespository;

public List<Bill> findAllBill() {
	// TODO Auto-generated method stub
	return iBillRespository.findAll();
}

public void deteleBillsById(long id) {
iBillRespository.deleteById(id);	
}

public Bill save(Bill bill) {
	
return iBillRespository.save(bill);	
}

	public List<Object> thongkethang(Date fromDate, Date toDate)
	{
		if(fromDate==null&&toDate==null)
		{
			return iBillRespository.thongke();
		}
		return iBillRespository.thongkeThang(fromDate,toDate);

	}
	public List<Object> thongkesanphamthang(Date fromDate, Date toDate, String kw)
	{
		if(fromDate==null&&toDate==null&&kw==null)
		{
			return iBillRespository.thongkeallsanpham();
		}
		return iBillRespository.thongkesanpham(fromDate,toDate,kw);
	}

	public List<Object> thongkeDTCTH()
	{

		return iBillRespository.thongkeDTCTH();
	}
	public List<Object> thongkeDTTT()
	{
		return iBillRespository.thongke();
	}
}
