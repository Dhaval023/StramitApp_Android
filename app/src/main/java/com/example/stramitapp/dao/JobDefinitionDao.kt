import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.stramitapp.model.JobDefinition
import com.example.stramitapp.model.MixJobDefinitionAssigment

@Dao
interface JobDefinitionDao {

    @Query("SELECT * FROM tbl_job_definition WHERE job_id = :jobId LIMIT 1")
    suspend fun getItem(jobId: Int): JobDefinition?

    @Query("""
        SELECT * FROM tbl_job_definition
        WHERE job_id = :jobId
        AND (:companyId IS NULL OR company_id = :companyId)
        AND (:locationId IS NULL OR location_id = :locationId)
    """)
    suspend fun getItems(jobId: Int, companyId: Int?, locationId: Int?): List<JobDefinition>

    @Query("""
        SELECT jd.job_id as jobId,
               jd.job_name as jobName,
               jd.company_id as companyId,
               jd.location_id as locationId,
               jd.job_desc as jobDesc,
               jd.creation_date as creationDate,
               jd.created_by as createdBy,
               jd.submittion_date as submittionDate,
               jd.update_flag as updateFlag,
               jd.last_update_date as lastUpdateDate,
               ja.status as assigStatus
        FROM tbl_job_definition jd
        INNER JOIN tbl_job_assignment ja
        ON ja.job_id = jd.job_id
        WHERE ja.assign_to = :assignTo
        AND ja.status IN (2,4,5)
    """)
    suspend fun getAcceptedJobs(assignTo: Int): List<MixJobDefinitionAssigment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: JobDefinition)

    @Delete
    suspend fun delete(item: JobDefinition)

    @Query("SELECT * FROM tbl_job_definition")
    suspend fun getAll(): List<JobDefinition>

    @Query("SELECT * FROM tbl_job_definition WHERE last_update_date > :lastSyncUpData")
    suspend fun getItemsToExport(lastSyncUpData: String): List<JobDefinition>
}