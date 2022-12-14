package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface TestRepository extends JpaRepository<Test, Long> {

    @Modifying
    @Transactional
    @Query("delete from Test where id = ?1")
    void deleteTestById(Long id);

    @Modifying
    @Transactional
    @Query("update Test set " +
            "testName = :testName where id = :id")
    void update(@Param("id") Long id,
                @Param("testName") String testName);
}
