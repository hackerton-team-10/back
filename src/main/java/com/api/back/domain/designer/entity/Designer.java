package com.api.back.domain.designer.entity;

import com.api.back.domain.designer.dto.response.DesignerResponse;
import com.api.back.global.common.BaseEntity;
import com.api.back.domain.reservation.type.ConsultationType;
import com.api.back.domain.designer.type.Region;
import com.api.back.domain.designer.type.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "designer")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Designer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String profile;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationType consultationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    private Integer consultingFeeVideo;

    private Integer consultingFeeInPerson;

    private String location;

    public DesignerResponse createDesignerResponse() {
        return DesignerResponse.builder()
                .id(this.getId())
                .name(this.getName())
                .profile(this.getProfile())
                .description(this.getDescription())
                .consultationType(this.getConsultationType())
                .region(this.getRegion())
                .specialty(this.getSpecialty())
                .consultingFeeVideo(this.getConsultingFeeVideo())
                .consultingFeeInPerson(this.getConsultingFeeInPerson())
                .location(this.getLocation())
                .build();
    }
}
