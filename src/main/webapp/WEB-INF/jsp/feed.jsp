<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<jsp:useBean id="myUtils" class="com.esliceu.utils.Util"/>
<html>
<head>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
    <title>Feed</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body style="margin: 0; padding: 0; box-sizing: border-box; width: 100%; background-color: #303030;">
<jsp:include page="header.jsp" />
<div style="width: 100%; margin: 75px auto;">
    <div style="text-align: center;">
        <a href="${pageContext.request.contextPath}/createNote" style="text-decoration: none; color: white;">
            <svg width="5em" height="5em" viewBox="0 0 16 16" class="bi bi-plus-circle" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd" d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                <path fill-rule="evenodd" d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>
            </svg>
        </a>
    </div>
</div>
<div style="width: 70%; display: flex; flex-wrap: wrap; flex-direction: row; justify-content: space-around; align-content: space-around; margin: auto;">
    <c:choose>
        <c:when test="${empty notes}">
            <div style="color: rgba(255, 255, 255, 0.6); font-size: large;">
                <p class="text-center font-italic">Looks like you don't have any notes yet! <br> You can start creating notes by clicking the '+' button above. </p>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Search form -->
            <form action="${pageContext.request.contextPath}/feed" method="get" class="form-inline d-flex justify-content-center md-form form-sm active-cyan active-cyan-2 mt-2" style="width: 60%; display: flex; flex-flow: row wrap; align-items: center; flex-basis: 100%;">
                <div class="md-form active-cyan active-cyan-2 mb-3" style="margin: 0 20px 0 20px;">
                    <input type="text" value="${inputValue}" name="searchValue" placeholder="Search" aria-label="Search" style="border: none; border-bottom: 2px solid #f5d742; background-color: #303030; color: white; border-radius: 0px;outline:none; width: 250px;">
                </div>
                <div style="margin: 0 20px 0 20px;">
                    <input type="text" name="dates" value="${dates}" style="width: 105%; background-color: #424242; margin-bottom: 16px; color: white; border-radius: .25rem; height: calc(1.5em + .75rem + 2px); padding: .375rem .75rem .375rem .75rem; font-weight: 400; line-height: 1.5; vertical-align: middle; border: 1px solid #ced4da; text-align: center;">
                    <script>
                        $('input[name="dates"]').daterangepicker();
                    </script>
                </div>
                <div class="md-form active-cyan active-cyan-2 mb-3" style="margin: 0 20px 0 20px;">
                    <select class="custom-select" id="inputGroupSelect01" name="searchType" style="background-color: #424242 !important; color: white !important;">
                        <option value="title-asc"  ${inputType == 'title-asc'  ? 'selected' : ''}>By title - ASC</option>
                        <option value="title-desc" ${inputType == 'title-desc' ? 'selected' : ''}>By title - DESC</option>
                        <option value="cdate-asc"  ${inputType == 'cdate-asc'  ? 'selected' : ''}>By creation date - ASC</option>
                        <option value="cdate-desc" ${inputType == 'cdate-desc' ? 'selected' : ''}>By creation date - DESC</option>
                        <option value="mdate-asc"  ${inputType == 'mdate-asc'  ? 'selected' : ''}>By modification date - ASC</option>
                        <option value="mdate-desc" ${inputType == 'mdate-desc' ? 'selected' : ''}>By modification date - DESC</option>
                    </select>
                </div>
                <button type="submit" style="border-radius: 50%; background-color: transparent; border: none; outline:none; margin-bottom: 16px;">
                    <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-search" fill="#f5d742" xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd" d="M10.442 10.442a1 1 0 0 1 1.415 0l3.85 3.85a1 1 0 0 1-1.414 1.415l-3.85-3.85a1 1 0 0 1 0-1.415z"/>
                        <path fill-rule="evenodd" d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zM13 6.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z"/>
                    </svg>
                </button>
            </form>

            <form action="${pageContext.request.contextPath}/deleteNote" method="post" style="width: 60%; display: flex; flex-flow: row wrap; align-items: center; flex-basis: 100%; justify-content: space-evenly;">
                <c:forEach var="c" items="${notes}">
                    <a href="${pageContext.request.contextPath}/detailNote?id=${c.noteid}" style="text-decoration: none; color: white; margin: 25px 35px;">
                        <c:choose>
                        <c:when test="${username.equals(c.getOwner().username)}">
                        <div class="card shadow p-3 mb-5 bg-white rounded" style="width: 18rem; height: 400px; background-color: #424242 !important;">
                            </c:when>
                            <c:otherwise>
                            <div class="card shadow p-3 mb-5 bg-white rounded" style="width: 18rem; height: 400px; background-color: #f5d74230 !important;">
                                </c:otherwise>
                                </c:choose>
                                <div class="form-check checkbox" id="checkbox" style="display: none;">
                                    <input class="form-check-input" type="checkbox" value="${c.noteid}" name="notesToDelete" id="multipleDelete">
                                </div>
                                <div class="card-body" style="padding: 1rem;">
                                    <div style="position: relative; top: 0; width: 100%;">
                                        <h5 class="card-title" style="text-overflow: ellipsis; white-space:nowrap; overflow:hidden">${c.title}</h5>
                                    </div>
                                    <div style="overflow: hidden; text-overflow: ellipsis;">
                                        <p class="card-text lead" style="display: -webkit-box; -webkit-line-clamp: 8; -webkit-box-orient: vertical; overflow: hidden;">
                                                ${myUtils.renderMarkdown2TEXT(c.body)}
                                        </p>
                                    </div>
                                    <div style="position: absolute; bottom: 0; font-size: small;">
                                        <c:choose>
                                            <c:when test="${username.equals(c.getOwner().username) || username.equals(c.getOwner().email)}">
                                                <p class="font-weight-light font-italic" style="margin-bottom: 5px;">Owner: me</p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="font-weight-light font-italic" style="margin-bottom: 5px;">Owner: ${c.getOwner().username}</p>
                                            </c:otherwise>
                                        </c:choose>
                                        <p class="font-weight-light font-italic" style="margin-bottom: 5px;">Edited ${DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a").format(c.lastModDate)} </p>
                                        <p class="font-weight-light font-italic" style="margin-bottom: 5px;">Created ${DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a").format(c.creationDate)} </p>
                                    </div>
                                </div>
                            </div>
                    </a>
                </c:forEach>
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <button type="button" id="button" class="btn btn-danger" style="position: absolute; left: 10%; top: 410px;">Multiple delete</button>
                <button type="button" class="btn btn-secondary" id="cancelButton" style="display: none; position: absolute; left: 10%; top: 460px;">Cancel</button>
                <!-- Modal -->
                <div class="modal fade" id="multipleDeleteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="exampleModalLongTitle" style="color: black;">Please confirm</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body" style="color: black;">
                                You will delete selected notes permanently
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-danger">Delete permanently</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <script>
                let checkboxes = document.querySelectorAll("input[type=checkbox][name=notesToDelete]");
                let enabledIDS = [];

                checkboxes.forEach(function(checkbox) {
                    checkbox.addEventListener('change', function() {
                        enabledIDS =
                            Array.from(checkboxes) // Convert checkboxes to an array to use filter and map.
                                .filter(i => i.checked) // Use Array.filter to remove unchecked checkboxes.
                                .map(i => i.value) // Use Array.map to extract only the checkbox values from the array of objects.
                    });
                });

                document.querySelector('#button').addEventListener('click', function() {
                    if(enabledIDS.length === 0 && document.querySelector('#checkbox').style.display === 'none'){
                        document.querySelector('#button').innerHTML = 'Delete selected';
                        document.querySelectorAll('.checkbox').forEach(x => x.style.display = 'block');
                        document.querySelector('#cancelButton').style.display = 'block';
                    }

                    if(enabledIDS.length > 0) {
                        document.querySelector('#button').setAttribute("data-toggle", "modal");
                        document.querySelector('#button').setAttribute("data-target", "#multipleDeleteModal");
                    }
                })

                document.querySelector('#cancelButton').addEventListener('click', function() {
                    document.querySelectorAll('.checkbox').forEach(x => x.style.display = 'none');
                    document.querySelector('#cancelButton').style.display = 'none';
                    document.querySelector('#button').innerHTML = 'Multiple delete';
                    document.querySelector('#button').removeAttribute("data-toggle");
                    document.querySelector('#button').removeAttribute("data-target");
                    enabledIDS = [];
                    checkboxes.forEach(c => c.checked = false);
                })
            </script>

        </c:otherwise>
    </c:choose>
</div>
<c:if test="${totalPages > 0}" >
    <nav aria-label="Page navigation example" style="margin-top: 50px; margin-bottom: 100px;">
        <ul class="pagination justify-content-center">
            <li class="page-item <c:if test="${previousPageAvailable == false}">disabled</c:if>">
                <a class="page-link" href="${pageContext.request.contextPath}/feed?searchValue=${inputValue}&dates=${dates}&searchType=${inputType}&page=${previousPage}" tabindex="-1">Previous</a>
            </li>
            <c:forEach var="c" begin = "0" end = "${totalPages}">
                <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/feed?searchValue=${inputValue}&dates=${dates}&searchType=${inputType}&page=${c+1}">${c+1}</a></li>
            </c:forEach>
            <li class="page-item <c:if test="${nextPageAvailable == false}">disabled</c:if>">
                <a class="page-link" href="${pageContext.request.contextPath}/feed?searchValue=${inputValue}&dates=${dates}&searchType=${inputType}&page=${nextPage}">Next</a>
            </li>
        </ul>
    </nav>
</c:if>
</body>
<script
        src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"
></script>
<script
        src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"
></script>
<script
        src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"
></script>
</html>
