package cdw.hk2.shop.laptop.dto;

import cdw.hk2.shop.laptop.model.Product;
import cdw.hk2.shop.laptop.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
public class CommentDTO {

//    private Product product;
    private  long id;
    private String user;
    private int rating;
    private String title;
    private String comment;
    private Date time;
    private String product;
}
