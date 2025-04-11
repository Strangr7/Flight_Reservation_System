<%@ taglib prefix="layout" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<layout:layout title="Flight Management" showSideNav="${true}">
    <jsp:attribute name="styles">
        <!-- CSS here -->
    </jsp:attribute>
    
    <jsp:attribute name="scripts">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- JS here -->
        <script>
        // Auto-dismiss after 5 seconds
        setTimeout(function() {
            document.querySelector('.alert').style.opacity = '0';
            setTimeout(function() {
                document.querySelector('.alert').remove();
            }, 500);
        }, 5000);
    </script>
    
    
    </jsp:attribute>
    
    <jsp:body>
    	<div class="container-fluid px-4">
    		<div class="d-flex justify-content-between align-items-center mb-4">
       			<h2><i class="bi bi-airplane me-2"></i>Flight Management</h2>
       			<div>
                    <span class="badge bg-secondary me-2">
                        Total Flights: ${totalItems}
                    </span>
                    <a href="${pageContext.request.contextPath}/flights/new" class="btn btn-outline-primary">
                        <i class="bi bi-plus-circle"></i> Add New Flight
                    </a>
                </div>
       		</div>
       
       
	       <c:if test="${not empty successMessage}">
	           <div class="alert alert-success">
	                ${successMessage}
	           </div>
	           <c:remove var="successMessage" scope="session"/>
	       </c:if> 
	       <div class="card shadow-sm">
	           <div class="card-body">
	               <form action="${pageContext.request.contextPath}/flights" method="get" class="mb-3">
		            <div class="row mb-3">
		                <div class="col-md-4">
		                    <input type="text" class="form-control" name="searchQuery" 
		                           placeholder="Search by flight number..." value="${param.searchQuery}">
		                </div>
		                <div class="col-md-3">
		                    <select class="form-select" name="statusFilter">
		                        <option value="">All Statuses</option>
		                        <option value="SCHEDULED" ${param.statusFilter == 'SCHEDULED' ? 'selected' : ''}>Scheduled</option>
		                        <option value="ON_TIME" ${param.statusFilter == 'ON_TIME' ? 'selected' : ''}>On Time</option>
		                        <option value="DELAYED" ${param.statusFilter == 'DELAYED' ? 'selected' : ''}>Delayed</option>
		                        <option value="COMPLETED" ${param.statusFilter == 'COMPLETED' ? 'selected' : ''}>Completed</option>
		                        <option value="CANCELLED" ${param.statusFilter == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
		                    </select>
		                        </div>
		                <div class="col-md-3">
		                    <input type="date" class="form-control" name="dateFilter" 
		                           value="${param.dateFilter}">
		                </div>
		                <div class="col-md-2">
		                    <button type="submit" class="btn btn-primary w-100">
		                        <i class="bi bi-search"></i> Search
		                    </button>
		                </div>
		            </div>
		        </form>
	           
       
       <%-- <div style="text-align: right;">
       	<a href="${pageContext.request.contextPath}/flights/new" class="btn btn-primary mt-3 ">
           <i class="bi bi-plus-circle"></i> Add New Flight
       </a>
       </div> --%>
       
	       <div class="table-responsive">
	       <table class="table table-hover flight-table">
	           <!-- Table content same as before -->
	           <thead class="table-light">
	               <tr>
	                   <th>ID</th>
	                   <th>Flight Number</th>
	                   <th>Airline</th>
	                   <th>Departure</th>
	                   <th>Arrival</th>
	                   <th>Duration</th>
	                   <th>Seats Available</th>
	                   <th>Status</th>
	                   <th>Actions</th>
	               </tr>
	           </thead>
	           <tbody>
	               <c:forEach items="${flights}" var="flight">
	                   <tr>
	                       <td>${flight.flightId}</td>
	                       <td>${flight.flightNumber}</td>
	                       <td>${flight.airline.airlineName}</td>
	                       <td>
	                          <%--  <fmt:formatDate value="${flight.departureTime}" pattern="MMM dd, HH:mm"/> --%>
	                           (${flight.departureAirport.airportCode})
	                       </td>
	                       <td>
	                           <%-- <fmt:formatDate value="${flight.arrivalTime}" pattern="MMM dd, HH:mm"/> --%>
	                           (${flight.destinationAirport.airportCode})
	                       </td>
	                       
	                <td>${flight.tripDuration} hours</td>
	                <td>${flight.seatsAvailable}</td>
	                <td>
	                    <span class="badge 
				                  <c:choose>
							          <c:when test="${fn:toUpperCase(flight.status) == 'SCHEDULED'}">bg-warning</c:when>
							          <c:when test="${fn:toUpperCase(flight.status) == 'COMPLETED'}">bg-success</c:when>
							          <c:when test="${fn:toUpperCase(flight.status) == 'ON_TIME'}">bg-success bg-opacity-75</c:when>
							          <c:when test="${fn:toUpperCase(flight.status) == 'DELAYED'}">bg-danger bg-opacity-75</c:when>
							          <c:when test="${fn:toUpperCase(flight.status) == 'CANCELLED'}">bg-danger</c:when>
	            					  <c:otherwise>bg-secondary</c:otherwise>
							      </c:choose>"
				         >
	                        ${fn:toUpperCase(flight.status)}
	                    </span>
	                </td>
	                       
	                       <td class="table-actions">
	                           <a href="${pageContext.request.contextPath}/flights/manage?id=${flight.flightId}" class="btn btn-sm btn-outline-primary">
	                               Edit
	                           </a>
	                           <button class="btn btn-sm btn-outline-danger" 
							          data-bs-toggle="modal" 
							          data-bs-target="#deleteModal"
							          data-flight-id="${flight.flightId}">
							      <i class="bi bi-trash"></i> Delete
							  </button>
	                        
	
	                       </td>
	                   </tr>
	               </c:forEach>
	           </tbody>
	       </table>
	       </div>
	       
       <!-- Pagination -->
        <nav aria-label="Flight pagination" class="float-end">
              <ul class="pagination justify-content-center">
                  <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                      <a class="page-link" 
                         href="${pageContext.request.contextPath}/flights?page=${currentPage - 1}&searchQuery=${param.searchQuery}&statusFilter=${param.statusFilter}&dateFilter=${param.dateFilter}">
                          Previous
                      </a>
                  </li>
                  
                  <c:forEach begin="1" end="${totalPages}" var="i">
                      <li class="page-item ${currentPage == i ? 'active' : ''}">
                          <a class="page-link" href="${pageContext.request.contextPath}/flights?page=${i}&searchQuery=${param.searchQuery}&statusFilter=${param.statusFilter}&dateFilter=${param.dateFilter}">${i}</a>
                      </li>
                  </c:forEach>
                  
                  <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                      <a class="page-link" 
                         href="${pageContext.request.contextPath}/flights?page=${currentPage + 1}&searchQuery=${param.searchQuery}&statusFilter=${param.statusFilter}&dateFilter=${param.dateFilter}">
                          Next
                      </a>
                  </li>
              </ul>
          </nav>
          </div>
	       </div>
	       </div>
            
    <!-- Delete Confirmation Modal -->

<div class="modal fade" id="deleteModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete this flight?
                <p class="text-danger">This action cannot be undone!</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <form id="deleteForm" action="${pageContext.request.contextPath}/flights/delete" method="post">
                    <input type="hidden" name="id" id="flightIdInput">
                    <button type="submit" class="btn btn-danger">
                        <i class="bi bi-trash-fill"></i> Delete
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    var deleteModal = document.getElementById('deleteModal');
    
    deleteModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget;
        // Extract info from data-flight-id attribute
        var flightId = button.getAttribute('data-flight-id');
        
        // Update the modal's input field
        var flightIdInput = document.getElementById('flightIdInput');
        flightIdInput.value = flightId;
    });
});
</script>
       
    </jsp:body>
    
    
    
</layout:layout>



