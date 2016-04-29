package com.hhly.mlottery.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.JsonReader;

import com.alibaba.fastjson.JSON;


/**
 * File Utils
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String)} read file</li>
 * <li>{@link #readFileToList(String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class FileUtils {

    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * read file
     * 
     * @param filePath
     * @return if file not exist, return null, else return content of file
     * @throws IOException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     * 
     * @param filePath
     * @param content
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return true
     * @throws IOException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     * 
     * @param filePath
     * @param stream
     * @return return true
     * @throws IOException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        OutputStream o = null;
        try {
            o = new FileOutputStream(filePath);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * read file to string list, a element of list is a line
     * 
     * @param filePath
     * @return if file not exist, return null, else return content of file
     * @throws IOException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     * 
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     * 
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     * 
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     * 
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     * 
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     * 
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     * 
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     * the directories can not be created.
     * <ul>
     * <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     * <li>if target directory already exists, return true</li>
     * <li>return {@link java.io.File}</li>
     * </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     * 
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     * 
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     * 
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * empty folder or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     * 
     * @param path
     * @return
     */
    public static boolean emptyFolder(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return true;
    }
    
    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     * 
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        if (StringUtils.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
    
    public static byte[] ReadFileBytes(String file) throws IOException {
		// Open file
		RandomAccessFile f = new RandomAccessFile(new File(file), "r");

		try {
			// Get and check length
			long longlength = f.length();
			int length = (int) longlength;
			if (length != longlength)
				throw new IOException("File size >= 2 GB");

			// Read file and return data
			byte[] data = new byte[length];
			f.readFully(data);
			return data;
		} finally {
			f.close();
		}		
	}
	
	public static InputStream ReadFileInputStream(String file) throws IOException {
		InputStream inStream = null;
		try{
			inStream = new FileInputStream(file);
		}catch(Exception e){
			e.printStackTrace();
		}
		return inStream;
	}

/*	public static Object ReadFromJsonData(byte[] buffer, Class cls) {
		if (null == buffer)
			return null;
		String data;
		try {
			data = new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		Gson gson = new Gson();
		Object obj;

		try {
			obj = gson.fromJson(data, cls);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}

		return obj;
	}*/
	
	public static <T> T ReadFromJsonData(byte[] buffer, Class<T> cls) {
		if (null == buffer || cls == null)
			return null;
		String data;
		try {
			data = new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		T t = JSON.parseObject(data, cls);

		return t;
	}
	
	public static Object ReadFromJsonData(byte[] buffer, Type type) {
		if (null == buffer || type == null)
			return null;
		String data;
		try {
			data = new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
        Object obj = JSON.parseObject(data, type);


		return obj;
	}

/*	public static Object ReadFromJsonFile(String fn, Class cls) {

		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(fn));
		} catch (FileNotFoundException e1) {
			return null;
		}

		Gson gson = new Gson();
		Object obj;

		try {
			obj = gson.fromJson(reader, cls);
		} catch (JsonIOException e) {
			e.printStackTrace();
			return null;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}

		return obj;
	}*/
	
	@SuppressWarnings("unchecked")
//	public static <T> T ReadFromJsonFile(String fn, Class<T> cls) {
//		if (null == fn || cls == null)
//			return null;
//		JsonReader reader;
//		try {
//			reader = new JsonReader(new FileReader(fn));
//		} catch (FileNotFoundException e1) {
//			return null;
//		}
//
//		Gson gson = new Gson();
//		T t;
//
//		try {
//			t = (T)gson.fromJson(reader, cls);
//		} catch (JsonIOException e) {
//			e.printStackTrace();
//			return null;
//		} catch (JsonSyntaxException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		return (T)t;
//	}

//	public static Object ReadFromJsonFile(String fn, Type type) {
//		if (null == fn || type == null)
//			return null;
//		JsonReader reader;
//		try {
//			reader = new JsonReader(new FileReader(fn));
//		} catch (FileNotFoundException e1) {
//			return null;
//		}
//
//		Gson gson = new Gson();
//		Object obj;
//
//		try {
//			obj = gson.fromJson(reader, type);
//		} catch (JsonIOException e) {
//			e.printStackTrace();
//			return null;
//		} catch (JsonSyntaxException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		return obj;
//	}
	
	public static void SaveJson(byte[] buffer, String fn) {
		if (null == buffer || fn == null)
			return;
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fn);
			fos.write(buffer);
			fos.flush();
			fos.close();				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将一个 byte[] 的数据保存到文件中。
	 * @param buffer 保存的字节数组
	 * @param fn 保存的文件名绝对路径
	 */
	public static void SaveToFile(byte[] buffer, String fn) {
		String fnx = fn + ".temp";
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fnx);
			fos.write(buffer);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (isFileExist(fnx)) {
			File file = new File(fnx);
			file.renameTo(new File(fn));
		}
	}

	/**
	 * 将一个 byte[] 的数据保存到文件中。
	 * @param buffer 保存的字节数组
	 * @param fn 保存的文件名绝对路径
	 */
	public static void SaveToFile2(byte[] buffer, String fn) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fn);
			fos.write(buffer);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (isFileExist(fn)) {
			File file = new File(fn);
			file.renameTo(new File(fn));
		}
	}
    /**
     * 打开文件
     * @param file
     */
//    public static boolean OpenFile(Context context, String path){
//		if (StringUtils.isBlank(path)) {
//            return false;
//        }
//        File file = new File(path);
//        if (!file.exists()) {
//            return false;
//        }
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //设置intent的Action属性
//        intent.setAction(Intent.ACTION_VIEW);
//        //获取文件file的MIME类型
//        String type = MediaUtils.getMIMEType(file);
//        //设置intent的data和Type属性。
//        intent.setDataAndType(Uri.fromFile(file), type);
//        //跳转
//        try {
//        	context.startActivity(intent);  
//		} catch (Exception e) {
//			//打开失败，提示找不到打开此文件的应用程序
//			UiUtils.toast(context, R.string.download_err_open);
//		}
//        return true;
//    }
    
	/**
	 * 获取SD卡的路径
	 */
	public static String getSDPath(){ 
		File sdDir = null;
		//判断sd卡是否存在
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if(sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		}
		return sdDir == null ? null : sdDir.toString();
	}
	
//	public static void main(String args[]) throws IOException {
//        // 源文件夹
//        String url1 = "D:/user/item_live_text/";
//        // 目标文件夹
//        String url2 = "D:/user/testcopy/";
//        // 创建目标文件夹
//        (new File(url2)).mkdirs();
//        // 获取源文件夹当前下的文件或目录
//        File[] file = (new File(url1)).listFiles();
//        for (int i = 0; i < file.length; i++) {
//            if (file[i].isFile()) {
//                // 复制文件
//                String type = file[i].getName().substring(file[i].getName().lastIndexOf(".") + 1);
//
//                if (type.equalsIgnoreCase("txt"))// 转码处理
//                    copyFile(file[i], new File(url2 + file[i].getName()), "uft-8", "gbk");
//                else
//                    copyFile(file[i], new File(url2 + file[i].getName()));
//            }
//            if (file[i].isDirectory()) {
//                // 复制目录
//                String sourceDir = url1 + File.separator + file[i].getName();
//                String targetDir = url2 + File.separator + file[i].getName();
//                copyDirectiory(sourceDir, targetDir);
//            }
//        }
//    }

    /**
     * 获取文件列表（可根据文件后缀名过滤）
     * @param sourceDir
     * @param filter eg:com.xxx.xxx | .zip |!lib !表示非
     * @return
     * @throws IOException
     */
    public static List<File> getFileList(String sourceDir, String... filters) {
    	List<File> filelist = new ArrayList<File>();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; file!=null && i < file.length; i++) {
            if (file[i].isAbsolute()) {
                // 源文件
                File sourceFile = file[i];
                // 过滤
                if(filters != null && !filters.equals("")){
                	boolean res = true;
                	for(String filter : filters){
                		if (filter.indexOf("!")!=-1) {
                			if(sourceFile.getName().contains(filter.substring(filter.indexOf("!")+1, filter.length()))){
                    			res = false;
                    			break;
                    		}
						}else{
							if(!sourceFile.getName().contains(filter)){
	                			res = false;
	                			break;
	                		}
						}
                	}
                	if(res){
                		filelist.add(sourceFile);
                	}
                }else{
                	filelist.add(sourceFile);
                }
            }
        }
        return filelist;
    }
    
    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
    
    // 复制文件夹
//    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
//    	copyDirectiory(sourceDir, targetDir, null);
//    }
    
    // 复制文件夹
//    public static void copyDirectiory(String sourceDir, String targetDir, String... filters) throws IOException {
//    	if(AppConstants.BACKUPS_STATE == AppConstants.BACKUPS_STATE_CANCEL) return;
//        // 新建目标目录
//        (new File(targetDir)).mkdirs();
//        // 获取源文件夹当前下的文件或目录
//        File[] files = (new File(sourceDir)).listFiles();
//        if (files != null) {
//	        for (int i = 0; i < files.length 
//	        		&& AppConstants.BACKUPS_STATE != AppConstants.BACKUPS_STATE_CANCEL; i++) {
//	        	boolean res = true;
//	        	if (filters != null && !filters.equals("")) {
//                	for(int j = 0; j < filters.length
//                			&& AppConstants.BACKUPS_STATE != AppConstants.BACKUPS_STATE_CANCEL; j++){
//                		String filter = filters[j];
//                		if (filter.indexOf("!")!=-1) {
//                			if(files[i].getName().contains(filter.substring(filter.indexOf("!")+1, filter.length()))){
//                    			res = false;
//                    			break;
//                    		}
//						}else{
//							if(!files[i].getName().contains(filter)){
//	                			res = false;
//	                			break;
//	                		}
//						}
//                	}
//				}
//	        	if (res) {
//	        		if (files[i].isFile()) {
//		                // 源文件
//		                File sourceFile = files[i];
//		                // 目标文件
//		                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + files[i].getName());
//		                copyFile(sourceFile, targetFile);
//		            }
//		            if (files[i].isDirectory()) {
//		                // 准备复制的源文件夹
//		                String dir1 = (sourceDir + "/" + files[i].getName()).replace("//", "/");
//		                // 准备复制的目标文件夹
//		                String dir2 = (targetDir + "/" + files[i].getName()).replace("//", "/");
//		                copyDirectiory(dir1, dir2);
//		            }
//				}
//	        }
//        }
//    }

    /**
     * 把文件转换为GBK文件
     * @param srcFileName
     * @param destFileName
     * @param srcCoding
     * @param destCoding
     * @throws IOException
     */
    public static void copyFile(File srcFileName, File destFileName, String srcCoding, String destCoding) throws IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileName), srcCoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFileName), destCoding));
            char[] cbuf = new char[1024 * 5];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } finally {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        }
    }

    /**
     * 
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
            }
        }
    }

	
	/**
	 * 获取目录文件大小
	 * @param dir
	 * @return
	 */
	public static long getDirectorySize(File dir) {
		if (dir == null) {
			return 0;
		}
	    if (!dir.isDirectory()) {
	    	return 0;
	    }
	    long dirSize = 0;
	    File[] files = dir.listFiles();
	    for (File file : files) {
	    	if (file.isFile()) {
	    		dirSize += file.length();
	    	} else if (file.isDirectory()) {
	    		dirSize += file.length();
	    		dirSize += getDirectorySize(file); //递归调用继续统计
	    	}
	    }
	    return dirSize;
	}
	
	/**
	 * 转换文件大小
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String FormatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
    
    /**
     * 创建目录
     * @param destDirName    目标目录名
     * @return    目录创建成功返回true，否则返回false
     */
    public static boolean createDir(String destDirName){
        File dir = new File(destDirName);
        if (dir.exists()) {
            ////System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return true;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            ////System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
           // //System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }

	/**
	 * 将数据缓存到文件
	 * @param context
	 * @param name
	 *            缓存文件名
	 * @param data
	 *            数据
	 */
	public static void saveDataToFile(Context context, String name, String data) {
		if (StringUtils.isEmpty(name)) return;
		String path = toCachePath(context, name);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			fos.write(data.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取缓存数据
	 * @param context
	 * @param name
	 *            缓存文件名称
	 * @param entity
	 *            需要转换为的实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T>T getCacheData(Context context, String name,Class<T> entity) {
		if (StringUtils.isEmpty(name)) return null;
		String path = toCachePath(context, name);
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(path));
		} catch (FileNotFoundException e1) {
			return null;
		}
        return JSON.parseObject(reader.toString(),entity);

	}
	/**
	 * 获取保存文件字符串数据
	 * @param context
	 * @param name
	 *            缓存文件名称

	 * @return
	 */
	public static String getCacheDataToString(Context context, String name) {
		if (StringUtils.isEmpty(name)) return null;
		String path = toCachePath(context, name);
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));  
		    String readline = "";  
		    StringBuffer sb = new StringBuffer();  
		    while ((readline = br.readLine()) != null) {  
//		        //System.out.println("readline:" + readline);  
		        sb.append(readline);  
		    }  
		    br.close();  
		    return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 删除缓存数据
	 * @param context
	 * @param name 缓存文件名称
	 * @return
	 */
	public static void deleteCacheData(Context context, String name) {
		if (StringUtils.isEmpty(name)) return;
		String path = toCachePath(context, name);
		try {
			del(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转换缓存文件路径
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static String toCachePath(Context context, String name) {
		return context.getFilesDir().getAbsolutePath() + "/" + name + ".txt";
	}

	/**
	 * 将字符串post请求数据体转换成请求所需结构体
	 * 
	 * @param data
	 * @return
	 */
	public static Map<String, InputStream> toPostDataMap(
			Map<String, String> data) {
		Map<String, InputStream> dataMap = new HashMap<String, InputStream>();
		List<String> keyList = new ArrayList<String>();
		for (String string : data.keySet()) {
			keyList.add(string);
		}
		for (int i = 0; i < data.size(); i++) {
			if (!StringUtils.isEmpty(data.get(keyList.get(i)))) {
				try {
					InputStream is = new ByteArrayInputStream(data.get(
							keyList.get(i)).getBytes("UTF-8"));
					dataMap.put(keyList.get(i), is);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return dataMap;
	}

	/**
	 * 将图片转换成输入流
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param filePath
	 * @param reqHeight
	 * @param reqWidth
	 * @return
	 */
	public static InputStream convertToBitmap(String filePath, int reqHeight,
			int reqWidth) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(filePath, options);

		final int height = options.outHeight;
		final int width = options.outWidth;

		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = inSampleSize;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		InputStream is = Bit2Input(bitmap);

		bitmap.recycle();
		bitmap = null;

		return is;
	}

	public static InputStream Bit2Input(Bitmap bm) {
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, bas);
		InputStream is = new ByteArrayInputStream(bas.toByteArray());
		return is;
	}


	/**
	 * 删除文件夹下所有文件
	* <p>Description: </p> 
	* @param file
	 */
	public static void deleteFile(File file) {}

}

