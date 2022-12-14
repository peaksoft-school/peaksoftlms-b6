package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface OptionRepository extends JpaRepository<Option, Long> {

    @Modifying
    @Transactional
    @Query("update Option set " +
            "optionValue = :option, " +
            "isTrue = :isTrue where id = :id")
    void update(@Param("id") Long id,
                @Param("option") String option,
                @Param("isTrue") Boolean isTrue);

}
