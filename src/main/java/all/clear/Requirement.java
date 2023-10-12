package all.clear;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Requirement {
    @Id @GeneratedValue
    @Column(name = "REQ_ID")
    private Long id;
}


