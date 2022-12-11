package cdw.hk2.shop.laptop.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cdw.hk2.shop.laptop.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cdw.hk2.shop.laptop.model.Review;
import cdw.hk2.shop.laptop.repository.IReviewRepository;

@Service
public class ReviewServices {
	@Autowired
	private IReviewRepository iReviewRepository;

	public List<Review> findAllReviews() {

		return iReviewRepository.findAll();
	}

	public List<CommentDTO> findAllReviews1() {

		List<Review> a = iReviewRepository.findAll();
		List<CommentDTO> b = new ArrayList<>();
		for(int i=0;i<a.size();i++)
		{
			CommentDTO cm = new CommentDTO();
			cm.setUser(a.get(i).getUser().getName());
			cm.setRating(a.get(i).getRating());
			cm.setTime(a.get(i).getTime());
			cm.setComment(a.get(i).getComment());
			cm.setTitle(a.get(i).getTitle());
			String pro = a.get(i).getProduct().getName();
			int index = pro.indexOf("(");

			cm.setProduct(pro.substring(0,index));
			cm.setId(a.get(i).getId());

			b.add(cm);

		}
		return  b;
	}

	public String deteleReviewsById(long id) {
		iReviewRepository.deleteById(id);;
		return "delete";
	}

	public Review saveReview(Review review) {
		return iReviewRepository.save(review);

	}

	public boolean checkReview(long idU, long idP) {
		Review review= iReviewRepository.existsByUserAndProduct(idU, idP);
		System.out.println();
		if(review!=null) {
			return false;
		}else {
			return true;

		}
	}
	public List<Review> findAllbyDate(Date date) {
		return iReviewRepository.findAllDate(date);
	}

}
