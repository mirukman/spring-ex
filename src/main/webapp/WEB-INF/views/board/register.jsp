<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@include file="../includes/header.jsp"%>


<div class="row">
  <div class="col-lg-12">
    <h1 class="page-header">Board Register</h1>
  </div>
  <!-- /.col-lg-12 -->
</div>
<!-- /.row -->

<style>
  .uploadResult {
    width: 100%;
    background-color: gray;
  }

  .uploadResult ul {
    display: flex;
    flex-flow: row;
    justify-content: center;
    align-items: center;
  }

  .uploadResult ul li {
    list-style: none;
    padding: 10px;
  }

  .uploadResult ul li img {
    width: 100px;
  }
</style>

<style>
  .bigPictureWrapper {
    position: absolute;
    display: none;
    justify-content: center;
    align-items: center;
    top: 0%;
    width: 100%;
    height: 100%;
    background-color: gray;
    z-index: 100;
  }

  .bigPicture {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>

<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default">

      <div class="panel-heading">Board Register</div>
      <!-- /.panel-heading -->
      <div class="panel-body">

        <form role="form" action="/board/register" method="post">
          <div class="form-group">
            <label>Title</label>
            <input class="form-control" name='title'>
          </div>

          <div class="form-group">
            <label>Content</label>
            <textarea class="form-control" rows="3" name='content'></textarea>
          </div>

          <div class="form-group">
            <label>Writer</label>
            <input class="form-control" name='writer' value='<sec:authentication property="principal.username" />' readonly="readonly">
          </div>

          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <button type="submit" class="btn btn-default">Submit Button</button>
          <button type="reset" class="btn btn-default">Reset Button</button>
        </form>

      </div>
      <!--  end panel-body -->

    </div>
    <!--  end panel-body -->
  </div>
  <!-- end panel -->
</div>
<!-- /.row -->


<div class="row">
  <div class="col-lg-12">
    <div class="panel panel-default">

      <div class="panel-heading">File Attach</div>
      <!-- /.panel-heading -->
      <div class="panel-body">
        <div class="form-group uploadDiv">
          <input type="file" name='uploadFile' multiple>
        </div>

        <div class='uploadResult'>
          <ul>

          </ul>
        </div>


      </div>
      <!--  end panel-body -->

    </div>
    <!--  end panel-body -->
  </div>
  <!-- end panel -->
</div>
<!-- /.row -->

<script type="text/javascript">
  $(document).ready(function (e) {

    const regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
    const maxSize = 5 * 1024 * 1024;

    function checkFile(fileName, fileSize) {
      if (fileSize >= maxSize) {
        return false;
      }

      if (regex.test(fileName)) {
        return false;
      }

      return true;
    }

    function showUploadResult(uploadResultArr) {
      if(!uploadResultArr || uploadResultArr.length == 0) {
        return;
      }

      const uploadUl = $(".uploadResult ul");
      let str = "";
      $(uploadResultArr).each(function(i, obj) {
        if(obj.image){
          const fileCallPath =  encodeURIComponent( obj.uploadPath + "/s_"+obj.uuid +"_"+obj.fileName);
          str += "<li data-path='"+obj.uploadPath+"'";
          str +=" data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'"
          str +" ><div>";
          str += "<span> "+ obj.fileName+"</span>";
          str += "<button type='button' data-file=\'"+fileCallPath+"\' "
          str += "data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
          str += "<img src='/upload/display?fileName="+fileCallPath+"'>";
          str += "</div>";
          str +"</li>";
        }else{
          const fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);
          str += "<li "
          str += "data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"' ><div>";
          str += "<span> "+ obj.fileName+"</span>";
          str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' " 
          str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
          str += "<img src='/resources/img/attach.png'></a>";
          str += "</div>";
          str +"</li>";
        }
      });
      uploadUl.append(str);
      uploadUl.on("click", "button", function(e) {
        console.log("delete file");
        const targetFile = $(this).data("file");
        const type = $(this).data("type");
        const targetItem = this.closest("li");

        $.ajax({
          url: "/upload/deleteFile",
          data: {fileName: targetFile, type: type},
          beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfToken);
          },
          dataType: 'text',
          type: 'post',
          success: function(result) {
            alert(result);
            targetItem.remove();
          }
        })
      });

    }

    var csrfHeaderName = "${_csrf.parameterName}";
    var csrfToken = "${_csrf.token}";

    $("input[type='file']").change(function (e) {

      const formData = new FormData();
      const inputFile = $("input[name='uploadFile']");
      const files = inputFile[0].files;

      for (let i = 0; i < files.length; i++) {

        if (!checkFile(files[i].name, files[i].size)) {
          alert(files[i].name + ": 용량초과 혹은 업로드 할 수 없는 확장자");
          return false;
        }

        formData.append("uploadFile", files[i]);
      }

      $.ajax({
        url: '/upload/uploadAjaxAction',
        processData: false,
        contentType: false,
        beforeSend: function(xhr) {
          xhr.setRequestHeader(csrfHeaderName, csrfToken);
        },
        data: formData,
        type: 'POST',
        dataType: 'json',
        success: function (result) {
          console.log(result);
          showUploadResult(result);
        }
      });
    });

    let formObj = $("form[role='form']");
    $("button[type='submit']").on("click", function(e) {
      e.preventDefault();
      
      let str = "";
      $(".uploadResult ul li").each(function(i, obj) {
        var jobj = $(obj);
        console.dir(jobj);

        str += "<input type='hidden' name='attachList[" + i + "].fileName' value='" + jobj.data("filename") + "'>";
        str += "<input type='hidden' name='attachList[" + i + "].uuid' value='" + jobj.data("uuid") + "'>";
        str += "<input type='hidden' name='attachList[" + i + "].uploadPath' value='" + jobj.data("path") + "'>";
        str += "<input type='hidden' name='attachList[" + i + "].fileType' value='" + jobj.data("type") + "'>";
      });

      formObj.append(str).submit();
    });

  });
</script>


<%@include file="../includes/footer.jsp"%>