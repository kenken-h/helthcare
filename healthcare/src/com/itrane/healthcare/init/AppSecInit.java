package com.itrane.healthcare.init;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * 作成した Spring Security 設定(WebSecConfig)を war に登録するために必要.
 * 
 * DelegatingFilterProxyを登録する. 
 * このクラスは、アプリ内のすべての URL に対して、
 * 自動的に springSecurityFilterChainフィルタを登録する。
 * 主なフィルタ：
 * ・HttpSessionContextIntegrationFilter：
 * 　Sessionにセキュリティ情報（SecurityContext）を設定する
 * 　SecurityContextの中には、認証手形（Authentication）などの情報を保持。
 * ・LogoutFilter                  ：ログアウト処理をする
 * ・AuthenticationProcessingFilter：認証処理をする
 * ・FilterSecurityInterceptor     ：アクセス制御をする
 * フィルタが他の WebApplicationInitializer インスタンスで追加される場合、
 * フィルタの順序をコントロールするために @Order を使用できる。
 * 
 * AbstractSecurityWebApplicationInitializerを拡張する以外に追加のコードは不要.
 */
public class AppSecInit extends AbstractSecurityWebApplicationInitializer {

}