package co.istad.reaksa.ecommerce.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String customerId;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Float discount;
    private String remark;
    @Column(nullable = false)
    private Boolean status;
    @Column(nullable = false)
    private LocalDateTime orderedAt;
    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;

}
