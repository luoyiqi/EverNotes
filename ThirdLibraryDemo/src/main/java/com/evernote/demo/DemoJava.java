package com.evernote.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Data;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Tag;
import com.evernote.thrift.transport.TTransportException;
import com.feifei.study.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

/**
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * clients是安卓的，client是java的
 * Android SDK 事实上就是在Java SDK的基础上增加了OAuth Activity，并且增加了一个异步的client wrapper。所以你有三种可选方案操作自己的笔记：
 * 1. 直接使用Java SDK
 * 2. 修改library/src/com/evernote/client/android/EvernoteSession.java, 根据Developer Token直接设置mAuthenticationResult的值
 * 3.申请key，然后激活
 */
public class DemoJava extends Activity {
        private static final String AUTH_TOKEN = "这里填写你申请的token";

    private UserStoreClient userStore;
    private NoteStoreClient noteStore;
    private String newNoteGuid;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main_activity);
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                init();
            }
        });
        thread.start();

    }

    private void init() {
        token = System.getenv("AUTH_TOKEN");
        if (token == null) {
            token = AUTH_TOKEN;
        }
        if ("".equals(token)) {
            Log.d("evernote", "请去申请Token开发环境或真实环境的");
            return;
        }

        try {
            //初始化
            test(token);
            //列出目录
//			listNotes();
            //创建一条笔记
            createNote();
            //搜索笔记
//			searchNotes();
            //更新标签
//			updateNoteTag();
        } catch (EDAMUserException e) {
            switch (e.getErrorCode()) {
                case AUTH_EXPIRED:
                    Log.d("evernote", "授权的Token已经过期");
                    break;
                case INVALID_AUTH:
                    Log.d("evernote", "无效的授权Token");
                    break;
                case QUOTA_REACHED:
                    Log.d("evernote", "无效的授权Token");
                    break;
                default:
                    Log.d("evernote", "错误： " + e.getErrorCode().toString() + " 参数： " + e.getParameter());
                    break;
            }
        } catch (EDAMSystemException e) {
            Log.d("evernote", "系统错误： " + e.getErrorCode().toString());
        } catch (TTransportException t) {
            Log.d("evernote", "网络错误：" + t.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化 UserStore and NoteStore 客户端
     */
    public void test(String token) throws Exception {
        // 设置 UserStore 的客户端并且检查和服务器的连接
        EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.YINXIANG, token);
        ClientFactory factory = new ClientFactory(evernoteAuth);
        userStore = factory.createUserStoreClient();

        boolean versionOk = userStore.checkVersion("Evernote EDAMDemo (Java)",
                com.evernote.edam.userstore.Constants.EDAM_VERSION_MAJOR,
                com.evernote.edam.userstore.Constants.EDAM_VERSION_MINOR);
        if (!versionOk) {
            Log.d("evernote", "不兼容的Evernote客户端协议");
        }

        // 设置 NoteStore 客户端
        noteStore = factory.createNoteStoreClient();
    }

    /**
     * 获取并显示用户的笔记列表
     */
    private void listNotes() throws Exception {
        Log.d("evernote", "Listing notes:");
        // 首先，获取一个笔记本的列表
        List<Notebook> notebooks = noteStore.listNotebooks();

        for (Notebook notebook : notebooks) {
            Log.d("evernote", "Notebook: " + notebook.getName());
            // 然后，搜索笔记本中前100个笔记并按创建日期排序
            NoteFilter filter = new NoteFilter();
            filter.setNotebookGuid(notebook.getGuid());
            filter.setOrder(NoteSortOrder.CREATED.getValue());
            filter.setAscending(true);

            NoteList noteList = noteStore.findNotes(filter, 0, 100);
            List<Note> notes = noteList.getNotes();
            for (Note note : notes) {
                Log.d("evernote", " * " + note.getTitle());
            }
        }
    }

    String enml_header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">" + "<en-note>";
    String enml_footer = "</en-note>";

    String todo = "";
    String enter = "<br/>";

    String greenText = "";
    String work = enml_header + "<span style=\"color:green;\">" + greenText + "</span><br/>"
            + "<en-todo/>" + todo + "<br/>" + enml_footer;

    /**
     * 创建一个新的笔记
     */
    private void createNote() throws Exception {
        Note note = new Note();
        note.setTitle("印象笔记测试：通过Java创建一个新的笔记");

		/*
         * //添加图片
		 * String fileName = "aa.png";
		 * String mimeType = "image/png";
		 * //首先创建一个资源对象，然后设置相关属性，比如文件名
		 * Resource resource = new Resource();
		 * resource.setData(readFileAsData(fileName));
		 * resource.setMime(mimeType);
		 * ResourceAttributes attributes = new ResourceAttributes();
		 * attributes.setFileName(fileName);
		 * resource.setAttributes(attributes);
		 * // 现在，给新的笔记增加一个新的资源对象
		 * note.addToResources(resource);
		 * // 这个资源对象作为笔记的一部分显示
		 * String hashHex = bytesToHex(resource.getData().getBodyHash());
		 */

        // Evernote笔记的内容是通过ENML语言生成的。
        String content = enml_header
                + "<span style=\"color:green;\">" + greenText + "</span><br/>"
                // + "<en-media type=\"image/png\" hash=\"" + hashHex + "\"/>"
                //  + "<en-todo/>第一条待办" + "<br/>" + "<en-todo checked=\"true\"/>第二条待办"
                //  + "<div>纯文本</div>"
                + enml_footer;
        note.setContent(content);
        // 最后，使用createNote方法，发送一个新的笔记给Evernote。
        // 返回的新笔记对象将包含服务器生成的属性,如新笔记的的GUID
        Note createdNote = noteStore.createNote(note);
        newNoteGuid = createdNote.getGuid();
        Log.d("evernote", "成功创建一个新的笔记，GUID为：" + newNoteGuid);
    }

    /**
     * 查询用户的笔记并显示结果
     */
    private void searchNotes() throws Exception {
        // 搜索的格式需要根据Evernote搜索语法，
        // 参考这里http://dev.evernote.com/documentation/cloud/chapters/Searching_notes.php

        // 在这个例子中，我们搜索的标题中包含XXX
        String query = "元发";

        // 搜索的笔记中包含具体标签，我可以使用这个：
        // String query = "tag:tagname";

        NoteFilter filter = new NoteFilter();
        filter.setWords(query);
        filter.setOrder(NoteSortOrder.UPDATED.getValue());
        filter.setAscending(false);

        // 查询前50个满足条件的笔记
        Log.d("evernote", "满足查询条件的笔记： " + query);
        NoteList notes = noteStore.findNotes(filter, 0, 50);
        Log.d("evernote", "找到 " + notes.getTotalNotes() + " 个笔记");
        // for (int i = 0; i < notes.getTotalNotes(); i++) {
        // Log.d("evernote", notes.getNotes().get(i).getTitle());
        // }

        Iterator<Note> iter = notes.getNotesIterator();
        while (iter.hasNext()) {
            Note note = iter.next();
            Log.d("evernote", "笔记： " + note.getTitle());
            // 通过findNotes()返回的Note对象，仅包含笔记的属性，标题，GUID，创建时间等，不包含笔记的内容和二进制数据，想获取可以调用getNote()方法
            Note fullNote = noteStore.getNote(note.getGuid(), true, true, false, false);
            Log.d("evernote", "笔记包含 " + fullNote.getResourcesSize() + " 个资源对象");
        }
    }

    /**
     * 更新标签分配给一个笔记。这个方法演示了如何调用updateNote方法发送被修改字段
     */
    private void updateNoteTag() throws Exception {
        // 当更新一个笔记时，只需要发送已经修改的字段。
        // 例如，通过updateNote发送的Note对象中没有包含资源字段，那么Evernote服务器不会修改已经存在的资源属性
        // 在示例代码中,我们获取我们先前创建的笔记,包括 笔记的内容和所有资源。
        Note note = noteStore.getNote(newNoteGuid, true, true, false, false);

        // 现在,更新笔记。复原内容和资源，因为没有修改他们。我们要修改的是标签。
        note.unsetContent();
        note.unsetResources();

        // 设置一个标签
        note.addToTagNames("TestTag");

        // 现在更新笔记，我们没有设置内容和资源，所以他们不会改变
        noteStore.updateNote(note);
        Log.d("evernote", "成功增加标签");
        // 证明一下我们没有修改笔记的内容和资源；重新取出笔记，它仍然只有一个资源（图片）
        note = noteStore.getNote(newNoteGuid, false, false, false, false);
        Log.d("evernote", "更新以后, 笔记有 " + note.getResourcesSize() + " 个资源");
        Log.d("evernote", "更新以后，笔记的标签是： ");
        for (String tagGuid : note.getTagGuids()) {
            Tag tag = noteStore.getTag(tagGuid);
            Log.d("evernote", "* " + tag.getName());
        }
    }

    /**
     * 从磁盘读取文件的内容并创建数据对象
     */
    @SuppressWarnings("unused")
    private Data readFileAsData(String fileName) throws Exception {
        String filePath = Environment.getExternalStorageDirectory().toString() + "/" + fileName;
        Log.d("evernote", filePath);
        // 读取文件的二进制内容
        FileInputStream in = new FileInputStream(filePath);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        byte[] block = new byte[10240];
        int len;
        while ((len = in.read(block)) >= 0) {
            byteOut.write(block, 0, len);
        }
        in.close();
        byte[] body = byteOut.toByteArray();

        // 创建一个新的包含文件内容的二进制对象
        Data data = new Data();
        data.setSize(body.length);
        data.setBodyHash(MessageDigest.getInstance("MD5").digest(body));
        data.setBody(body);

        return data;
    }

    /**
     * 把byte数组转换成hexadecimal字符串
     */
    @SuppressWarnings("unused")
    private String bytesToHex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for (byte hashByte : bytes) {
            int intVal = 0xff & hashByte;
            if (intVal < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(intVal));
        }
        return sb.toString();
    }
}
