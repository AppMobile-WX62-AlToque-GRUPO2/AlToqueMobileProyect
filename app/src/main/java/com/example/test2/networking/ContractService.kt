import com.example.test2.models.Contract
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ContractService {
    @GET("contracts")
    fun getContractsByState(
        @Query("state") state: Int
    ): Call<List<Contract>>
}