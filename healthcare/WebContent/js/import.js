jQuery(function($) {
	var options = {
			bg: '#79f',
			// leave target blank for global nanobar
			target: document.getElementById('myDivId')
	};
	var nanobar = new Nanobar(options);

	// Bootstrap プログレスバーに進捗％を設定する
	function setProgress(p) {
		nanobar.go(p);
	}
	// "インポート"ボタンが押されたときに実行
	$("#subbutton").click(function() {
		if (typeof files != "undefined") {
			// ファイルタイプがエクセルの場合だけアップロードを行う
			if (files[0].type.slice(-5) == "excel") {
				uploadFile();
			} else {
				$('#info').html("ファイルが不正：エクセルファイルを選択してください");
				$('#info').css('color', '#ff0000')
			}
		}
	});
	// 選択ファイルの取得：プログレスバー、メッセージをリセットする
	$("#file1").on('change', getFile);
	var files;
	function getFile(event) {
		files = event.target.files;
		percent = 0;
		setProgress(percent);
		$('#info').html("");
	}
	// ファイルのアップロード
	function uploadFile() {
		var fd = new FormData();
		fd.append("file", files[0]);
		$.ajax({
			dataType : 'json',
			url : "/healthcare/importExcel",
			data : fd,
			type : "POST",
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false,
			xhr : function() {
				var xhr = new window.XMLHttpRequest();
				// Upload progress
				xhr.upload.addEventListener("progress", function(evt) {
					if (evt.lengthComputable) {
						var p = evt.loaded / evt.total;
						setProgress(Math.round(p * 100));
					}
				}, false);
				return xhr;
			},
			success : function(response) {
				$('#info').html(response);
				var str = response.split("：");
				if (str[0] == "アップロード成功") {
					setProgress(100);
					$('#info').css('color', 'green')
				} else {
					setProgress(0);
					$('#info').css('color', '#ff0000')
				}
			},
			error : function(response) {
				percent = 0;
				setProgress(percent);
				$('#info').html(response);
				$('#info').css('color', '#ff0000')
			}
		});
	}
});
