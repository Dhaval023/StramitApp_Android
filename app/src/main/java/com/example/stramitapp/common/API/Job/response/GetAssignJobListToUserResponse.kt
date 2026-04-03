package com.example.stramitapp.common.API.Job.response

data class GetAssignJobListToUserResponse(
    val statusCode: Int,
    val error: Any?,
    val success: Any?,
    val databaseTimeStamp: Any?,
    val list: List<JobItem>?
) {

    data class JobItem(
        val jobId: Int,
        val tblCompanyLocation: TblCompanyLocation?,
        val jobName: String?,
        val jobDesc: String?,
        val companyId: Int,
        val creationDate: String?,
        val createdBy: Int,
        val status: Int,
        val submittionDate: Any?,
        val locationId: Int,
        val deviceUdid: String?,
        val companyName: Any?,
        val createdByUser: Any?,
        val locationName: Any?,
        val assignByUser: Any?,
        val assignByUserEmail: Any?,
        val costCenterId: Int,
        val costCenterName: Any?,
        val statusName: Any?,
        val isJobAssignVisible: Int,
        val isAssignJob: Int,
        val isDeleteAssignJob: Int,
        val jobCloseDate: Any?,
        val responsibleUser: Any?
    )

    data class TblCompanyLocation(
        val locationId: Int,
        val locationName: String?,
        val isRootLocation: Int,
        val barcode: String?,
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int,
        val parentLocationId: Int
    )
}