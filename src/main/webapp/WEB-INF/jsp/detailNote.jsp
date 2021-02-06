<%--
  Created by IntelliJ IDEA.
  User: Dani
  Date: 11/11/2020
  Time: 22:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="myUtils" class="com.esliceu.utils.Util"/>
<html>
<head>
    <title>Note</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <style>
        .accordion-button{
            color: white !important;
        }
        .accordion-button:not(.collapsed) {
            background-color: #424242 !important;
            color: white !important;
        }
    </style>
</head>

<body style="margin: 0; padding: 0; box-sizing: border-box; width: 100%; background-color: #303030; color: white;">

<jsp:include page="header.jsp"/>

<c:choose>
    <c:when test="${not empty note}">
        <div style="width: 100%; padding: 50px 100px; display: flex; flex-flow: row wrap; justify-content: space-between;">
            <c:set var="isowner" value="${ownership}"/>
            <c:set var="iseditor" value="${permissionMode == 'EDITOR'}"/>
            <c:set var="isviewer" value="${permissionMode == 'VIEW'}"/>

            <c:if test="${isviewer == false}">
                <div style="flex-basis: 20%; background-color: #424242 !important; ">
                    <div class="accordion" id="accordionExample">
                        <c:forEach var="c" items="${versionList}" varStatus="pointer">
                            <div class="accordion-item">
                                <h2 class="accordion-header" id="heading${pointer.index+1}">
                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapse${pointer.index+1}" aria-expanded="true"
                                            aria-controls="collapse${pointer.index+1}">
                                        Created ${DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a").format(c.creationDate)}
                                    </button>
                                </h2>
                                <div id="collapse${pointer.index+1}" class="accordion-collapse collapse show"
                                     aria-labelledby="heading${pointer.index+1}"
                                     data-bs-parent="#accordionExample">
                                    <div class="accordion-body">
                                        <strong>This is the first item's accordion body.</strong> It is hidden by
                                        default,
                                        until
                                        the collapse plugin adds the appropriate classes that we use to style each
                                        element.
                                        These classes control the overall appearance, as well as the showing and hiding
                                        via
                                        CSS
                                        transitions. You can modify any of this with custom CSS or overriding our
                                        default
                                        variables. It's also worth noting that just about any HTML can go within the
                                        <code>.accordion-body</code>,
                                        though the transition does limit overflow.
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <div style="flex-basis: 40%;">
                <c:if test="${isviewer == true}">
                <fieldset disabled>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/edit?id=${note.noteid}"
                       style="cursor: pointer; text-decoration: none; color: white;">
                        <div style="background-color: #424242 !important; border-radius: 5px;">
                            <div class="form-group" style="height: 50px; padding: 1rem 2rem 0 2rem;">
                                <h5 style="background-color: #424242 !important; border: none; color: white; font-size: larger; width: 100%; outline: none; text-overflow: ellipsis; white-space:nowrap; overflow:hidden">${note.title}</h5>
                            </div>
                            <div class="form-group" style="padding: 1rem 2rem 1rem 2rem; overflow-wrap: anywhere;">
                                    ${note.body}
                            </div>
                        </div>
                    </a>
                    <c:if test="${isviewer == true}">
                    <small style="margin-bottom: 15px;" id="emailHelp" class="form-text text-muted">You are not owner so
                        you can't edit or share this note. If you don't want to see this note you can delete it from
                        your feed.</small>
                </fieldset>
                </c:if>

                <form class="form-inline mt-3 mb-3" action="${pageContext.request.contextPath}/shareNote" method="POST"
                      style="display: flex; flex-flow: row wrap; width: 100%; justify-content: space-between;">
                    <c:if test="${isviewer == true}">
                    <fieldset disabled>
                        </c:if>

                        <div class="input-group mr-3">
                            <div class="input-group-prepend">
                                <div class="input-group-text">@</div>
                            </div>
                            <input name="option" type="text" class="form-control" id="inlineFormInputGroupUsername2"
                                   placeholder="Username">
                        </div>

                        <div class="input-group mr-3">
                            <label class="my-1 mr-2" for="inputGroupSelect01">Permission mode</label>
                            <select class="custom-select my-1 mr-sm-2" id="inputGroupSelect01" name="permissionMode">
                                <option value="VIEW" selected>View</option>
                                <option value="EDITOR">Editor</option>
                            </select>
                        </div>


                        <input type="hidden" name="noteid" value="${note.noteid}">
                        <input type="hidden" name="_csrftoken" value="${csrfToken}">
                        <button type="submit" class="btn btn-primary my-1">Share</button>
                        <c:if test="${isviewer == true}">
                    </fieldset>
                    </c:if>
                </form>
                <c:if test="${isviewer == true}">
                    </fieldset>
                </c:if>

                <c:if test="${not empty usersShared}">
                    <div style="display: flex; flex-flow: row wrap; width: 100%; align-items: center;">
                        <p style="margin-right: 10px;">Shared with: </p>
                        <c:forEach var="c" items="${usersShared}">
                            <form action="/deleteUserFromSharedNote" method="POST"
                                  style="display: flex; flex-flow: row wrap; align-items: center; padding: 5px; background-color: #424242 !important; align-self: center; vertical-align: middle; border: 1px solid rgba(255, 255, 255, 0.6); border-radius: 10px; margin-right: 10px;">
                                <input type="hidden" name="userid" value="${c.userid}">
                                <input type="hidden" name="noteid" value="${note.noteid}">
                                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                                <p style="margin: auto 7px;">@${c.username}</p>
                                <button type="submit" value="Submit"
                                        style="border-radius: 100%; background-color: transparent; border: none;">
                                    <svg width="15px" height="15px" viewBox="0 0 16 16" class="bi bi-x-circle"
                                         fill="white" xmlns="http://www.w3.org/2000/svg">
                                        <path fill-rule="evenodd"
                                              d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                                        <path fill-rule="evenodd"
                                              d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                                    </svg>
                                </button>
                            </form>
                        </c:forEach>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/deleteNote" method="POST">
                    <input type="hidden" name="notesToDelete" value="${note.noteid}">
                    <input type="hidden" name="_csrftoken" value="${csrfToken}">

                    <button type="submit" class="btn btn-danger" data-toggle="modal" data-target="#exampleModalCenter"
                            style="display: flex; flex-flow: row nowrap; align-items: center;">
                        Delete
                        <svg width="1em" height="1em" viewBox="0 0 16 16" class="bi bi-trash-fill" fill="currentColor"
                             xmlns="http://www.w3.org/2000/svg">
                            <path fill-rule="evenodd"
                                  d="M2.5 1a1 1 0 0 0-1 1v1a1 1 0 0 0 1 1H3v9a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V4h.5a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H10a1 1 0 0 0-1-1H7a1 1 0 0 0-1 1H2.5zm3 4a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7a.5.5 0 0 1 .5-.5zM8 5a.5.5 0 0 1 .5.5v7a.5.5 0 0 1-1 0v-7A.5.5 0 0 1 8 5zm3 .5a.5.5 0 0 0-1 0v7a.5.5 0 0 0 1 0v-7z"/>
                        </svg>
                    </button>

                    <!-- Modal -->
                    <div class="modal fade" id="exampleModalCenter" tabindex="-1" role="dialog"
                         aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLongTitle" style="color: black;">Please
                                        confirm</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <c:choose>
                                    <c:when test="${isviewer == true}">
                                        <div class="modal-body" style="color: black;">
                                            You will not see this note.
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="modal-body" style="color: black;">
                                            You're going to delete this note permanently.
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <c:choose>
                                        <c:when test="${isviewer == true}">
                                            <button type="submit" class="btn btn-danger">Delete</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" class="btn btn-danger">Delete permanently</button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>


            <div style="flex-basis: 20%;"></div>
        </div>


    </c:when>
    <c:otherwise>
        <div style="width: 40%; margin: 150px auto;">
            <form action="${pageContext.request.contextPath}/createNote" method="POST">
                <div style="background-color: #424242 !important; border-radius: 5px;">
                    <div class="form-group" style="height: 50px; padding: 1rem 2rem 0 2rem;">
                        <input type="text" id="exampleFormControlInput2" name="title" placeholder="Title"
                               style="background-color: #424242 !important; border: none; color: white; font-size: larger; width: 100%; outline: none;">
                    </div>
                    <div class="form-group" style="padding: 1rem 2rem 0 2rem;">
                        <textarea id="exampleFormControlTextarea2" rows="15" name="body" placeholder="Body"
                                  style="background-color: #424242 !important; border: none; color: white; width: 100%; outline: none;"></textarea>
                    </div>
                </div>
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <button type="submit" class="btn btn-success">Create</button>
            </form>
        </div>
    </c:otherwise>
</c:choose>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW"
        crossorigin="anonymous"></script>
</body>


</html>
