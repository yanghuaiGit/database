package com.example.demo.kerberos;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.security.PrivilegedAction;

public class KerberosClient {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.krb5.conf", "/Users/yanghuai/kerberos/local/krb5.conf");
        String loginModuleName = "KerberosLogin";
        Krb5Configuration conf = new Krb5Configuration();
        try {
            String sep = File.separator;
            System.out.println("KerberosClient.main():"
                    + System.getProperty("java.home") + sep + "lib" + sep
                    + "security" + sep + "java.security");
            LoginContext context = new LoginContext("myKerberosLogin", null,
                    null, conf);
            //权限认证
            context.login();
            System.out.println(context.getSubject().getPrincipals());

            //用户的业务逻辑
            //java的权限认证就和用户的业务逻辑分离了
            Subject.doAs( context.getSubject(), new PrivilegedAction() {
                @Override
                public Object run() {
                    System.out.println(context.getSubject());
                    return null;
                }
            });



        } catch (LoginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
