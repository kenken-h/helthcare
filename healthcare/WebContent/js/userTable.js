jQuery(function($) {
	var fBtn = false;
    var oTable = $("#dtTable").dataTable({
    	"processing": true,	  //処理中の表示
    	"serverSide": true,   //サーバーサイド処理
    	"lengthChange": true, //ページ表示行数を変更
    	"lengthMenu": [[10, 20,],[10, 20]], //ページ表示行数の項目設定
    	"ajax": {
    		"url" : "userPage",
    		"contentType": "application/json",
    	    "data": function ( d ) {
    	    	return JSON.stringify(d);
    	    }
    	},
    	"columns": [
    	    		 {"data": "name", "width": 100 },
    	    		 {"data": "simeiSei", "width": 80 },
    	    		 {"data": "simeiMei", "width": 80 },
    	    		 {"data": "roles", "width": 160 },
    	    		 {
    	                "data": "id", "class": "center", "width": 160, "orderable": false,
    	     		 	"createdCell": function (td, cellData, rowData, row, col) {
    	     		 		var bd = $('<button class="btn_delete">削除</button>');
    	     		 		bd.button();
    	     		 		bd.on('click',function(){
    	     		 			fBtn = true;
    	     		 			alert("Delete:"+cellData);
    	                   });
    	     		 		var be = $('<button class="btn_edit">編集</button>');
    	     		 		be.button();
    	     		 		be.on('click',function(){
    	     		 			fBtn = true;
    	     		 			//ajax で get して成功時はダイアログを表示
    	     		 			$.getJSON("editUser/" + cellData , {}, 
    	     		 				function(user){
    	     		 					showModal("選択したユーザーを修正してください",
    	     		 							user.id, user.version, user.name,
    	     	     		 					user.simeiSei, user.simeiMei, user.roles);
    	     		 			});
    	                    });
    	     		 		$(td).empty();
    	                    $(td).prepend(bd);    		 		
    	                    $(td).prepend(be);
    	    			 }
    	    		 }
    	    	],
    	    	"language": japaneseLang,
    	    	"pagingType" : "full_numbers"	//ページングボタンの表示タイプ
    }).api(); //dataTables
    
    //追加ボタンが押されたらモーダルを新規モードで表示
    $("#btn-create").click(function () {
    	//oTable.search('next').draw();
    	showModal("新規ユーザーを追加してください。","", 0, "", "", "", "");
    });
    //モーダルの保存ボタン押下時の処理
	$('#btnSave').click(function(){
		$('#modalForm').find('span').remove();
		var cmd = {
				"user": {
					"id": $('#id').val(),
					"version": $('#version').val(),
					"name": $('#name').val(),
					//管理者が保存する場合はパスワードは常にユーザー名と同じ
					"ipass": $('#name').val(),					
					"simeiSei": $('#simeiSei').val(),
					"simeiMei": $('#simeiMei').val(),
					"roles": getRoles()
				},
				"status": 0,
		}
		$.ajax({
		    method: 'post',
		    contentType: 'application/json;charset=utf-8',
		    data: JSON.stringify(cmd),
		    url: 'save.user',
		    dataType: 'json'
		  }).done(function(cmd) {
			  if (cmd.status==1) {
				  //alert("成功したらモーダルを閉じる");
				  $('#myModal').modal('hide');
				  //oTable.page(cmd.page).draw(false);
				  oTable.draw();
			  } else {
				  //alert("検証エラーならエラーメッセージを表示");
					for (var key in cmd.fldErrors) {
						$('#'+key).after('<span>'+cmd.fldErrors[key]+'</span>')
					}
			  }
		  });
		//$('#modalForm').submit();
	});

	//テーブルのクリック行の選択状態をトグルする
	$('#dtTable tbody').on( 'click', 'tr', function () {
    	if (fBtn==false) {
        	$(this).toggleClass('selected');
    	} else {
    		fBtn = false;
    	}
    } );
	//roles チェックボックスからロールのカンマ区切り文字列を取得
	function getRoles() {
		return $('#roles').find('input:checkbox:checked').map(function() {
			  return $(this).val();
		}).get().join(",");
	}	
	//モーダルを表示する
	function showModal(title, id, version, name, sei, mei, roles) {
    	var $form = $('#modalForm');
    	$form.find('span').remove();
		$("#myModalLabel").html(title);
		setUser(id, version, name, sei, mei, roles);	 	
	 	$('#myModal').modal('show');
	}
	//モーダル内のフォームに渡された値を設定する
    function setUser(id, version, name, sei, mei, roles) {
    	$("#id").val(id);
    	$("#version").val(version);
		$("#name").val(name);
		$("#simeiSei").val(sei);
		$("#simeiMei").val(mei);
		$("#ROLE_ADMIN").prop('checked', false);
		$("#ROLE_USER").prop('checked', false);
		roleArray = roles.split(",");
		$.each(roleArray, function(index, value) {
			$("#"+value).prop('checked', true);
		});
    }
});
