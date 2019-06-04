package UI;

import BLL.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.LayoutManager;

public class MainForm extends JFrame implements ActionListener{
	public static MainForm _instance;
	//界面分类
	JPanel p, SortPanel, ActionPanel, ShowPanel, FunctPanel, TreePanel;
	//树逐渐
	JTree BigTree;
	
//	FilesTree filesTree;
	//轻量级文件试图，包含滚动条等.
	JScrollPane ScrollShow, TreeShow;
	//单选按钮，显示那种类型的文件
	JRadioButton AllFiles,Videos,Text,Picture,Music,BtnQian;
	//树节点
    DefaultMutableTreeNode node;
    //按钮
	JButton PreBtn, LatBtn, GoBtn,SearchDir,SetName;
	//
	ButtonGroup Classify;
	//下拉列表组件
	JComboBox SortList, SortType;
	//单行文本框
	JTextField SearchText, GuideText,PositionUrl,Revalue;
	//选择框
	JCheckBox FileCheck, DirCheck;
	//标签组件
	JLabel SortTxt, SearchTxt, SearchType;
	//排序依据
	String Sort_Items[] = {"文件大小","修改时间","首字母"};
	//排序类型
	String Sort_Type_Items[] = {"升序","降序"};
	public String Cur_URL = "";
	String Pre_URL = "";
	String LatURL = "";
	String FileURL = "";
	//各类文件格式匹配的初始化
	List<String> VideoType = Arrays.asList("avi","wmv","rm","rmvb","mpeg1","mpeg2","mpeg4","mp4","3gp","asf","swf","vob","dat","mov","m4v","flv","f4v","mkv","mts","ts","qsv","AVI","WMV","RM","RMVB","MPEG1","MPEG2","MPEG4","MP4","3GP","ASF","SWF","VOB","DAT","MOV","M4V","FLV","F4V","MKV","MTS","TS","QSV");  
	List<String> GraphType = Arrays.asList("bmp","gif","jpeg","jpeg2000","tiff","psd","png","swf","svg","pcx","dxf","wmf","emf","lic","eps","tga","jpg","BMP","GIF","JPEG","JPEG2000","TIFF","PSD","PNG","SWF","SVG","PCX","DXF","WMF","EMF","LIC","EPS","TGA","JPG");
	List<String> TxtType = Arrays.asList("txt","doc","docx","wps","pdf","chm","pdg","wdl","xls","xlsx","ppt","pptx","java","c","cpp","py");
	List<String> MusicType = Arrays.asList("cd","wave","wav","aiff","au","mp3","midi","wma","aac","ape","CD","WAVE","WAV","AIFF","AU","MP3","MIDI","WMA","RealAudio","VQF","OggVorbis","AAC","APE");
	public Map<String, String> Maps = new HashMap<String,String>();
	//文件列表的相关变量
	JList<String> list;
	public DefaultListModel defaultListModel;
	public Stack<String> stack, stack_return;
	//新建右键弹出菜单，选在在线面添加
	JPopupMenu jPopupMenu = null;
	JPopupMenu jPopupMenu2 = null;
	JPopupMenu jPopupMenu3 = null;
	//新建右键弹出项，没有定义内容
	JMenuItem[] JMIs = new JMenuItem[10];
	JMenuItem[] JMIs2 = new JMenuItem[5];
	JMenuItem[] JMIs3 = new JMenuItem[5];
	public Icon[] AllIcons = new Icon[999999];//存储搜索得到的文件图标
	public int Icon_Counter = 0;
	//保存GB,MB,KB,B对应的字节数，方便换算文件大小及单位
	long[] Sizes = {1073741824,1048576,1024,1};
	String[] Size_Names = {"GB", "MB", "KB", "B"};
	Boolean isSearching = false;
	private JPanel panel;
	
	public MainForm(){//主界面
		this._instance = this;
		//设置窗口名
		this.setTitle("文件整理系统");
		//设置窗口的x位置、y位置、宽、高
		this.setBounds(500, 500, 1200, 650);
		//初始化容器、默认布局
		this.getContentPane().setLayout(null);
		//调用函数添加组件
		Init();
		//将窗口至于屏幕中间
		this.setLocationRelativeTo(null);
		//显示组件JFrame
		this.setVisible(true);
		//单击关闭时的操作
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//不可调整窗口大小
		this.setResizable(false);
	}
	
	public void Init(){
		//各个panel初始化
		p = new JPanel();
		SortPanel = new JPanel();
		ActionPanel = new JPanel();
		ShowPanel = new JPanel();
		FunctPanel = new JPanel();
		TreePanel = new JPanel();
		//设置组件坐标和宽高，设置流式布局
		p.setBounds(0, 50, 350, 100);
		p.setLayout(new FlowLayout(FlowLayout.LEFT,15,5));
    
		//顶部查询搜索模块
		SortPanel.setSize(200, 80);
        SortPanel.setLayout(new FlowLayout());
        
        //类别JRadioButton初始化
        AllFiles = new JRadioButton("所有文件");
        AllFiles.setSelected(true);
		Videos = new JRadioButton("视频");
		Picture  = new JRadioButton("图片");
		Music = new JRadioButton("音乐");
		Text = new JRadioButton("文档");
		Videos.addActionListener(this);
		Picture.addActionListener(this);
		Music.addActionListener(this);
		Text.addActionListener(this);
		AllFiles.addActionListener(this);
		
		Classify = new ButtonGroup();
		Classify.add(AllFiles);
		Classify.add(Videos);
		Classify.add(Picture);
		Classify.add(Music);
		Classify.add(Text);
		
		SortTxt = new JLabel("排序");
		SortList = new JComboBox(Sort_Items);
		SortType = new JComboBox(Sort_Type_Items);
		SortList.addActionListener(this);
		SortType.addActionListener(this);
		SortPanel.add(SortTxt);
		SortPanel.add(SortList);
		SortPanel.add(SortType);
		
//		SearchTxt = new JLabel("搜索");
//		SearchTxt.setBounds(5,5,50,30);
//		SearchText = new JTextField(15);
//		SearchText.setBounds(50, 5, 120, 30);
//		SearchText.addActionListener(this);		
		
//		SearchType = new JLabel("搜索类型");		
//		FileCheck = new JCheckBox("文件");
//		FileCheck.setSelected(true);
//		DirCheck = new JCheckBox("目录");
//		DirCheck.setSelected(true);
//		FileCheck.addActionListener(this);
//		DirCheck.addActionListener(this);
		
		p.add(AllFiles);
		p.add(Videos);
		p.add(Picture);
		p.add(Music);
		p.add(Text);
		p.add(SortPanel);
//		p.add(SearchTxt);
//		p.add(SearchText);	
//		p.add(SearchType);
//		p.add(FileCheck);
//		p.add(DirCheck);
	   
		 //中上导航栏35
        FunctPanel = new JPanel();
        FunctPanel.setBounds(30, 10, 1200, 45);
        FunctPanel.setLayout(null);
        PreBtn = new JButton("<");
        PreBtn.setFont(new Font("Serif", Font.PLAIN, 20));
        PreBtn.setBounds(15, 5, 85, 25);
        PreBtn.addActionListener(this);
        LatBtn = new JButton(">");
        LatBtn.setFont(new Font("Serif", Font.PLAIN, 20));
        LatBtn.setBounds(125, 5, 85, 25);
        LatBtn.addActionListener(this);
        GuideText = new JTextField();
        GuideText.setBounds(300, 5, 650, 25);
        GuideText.addActionListener(this);
        GoBtn = new JButton("Go!");
        GoBtn.setFont(new Font("Serif", Font.PLAIN, 15));
        GoBtn.setBounds(975, 5, 65, 25);
        GoBtn.addActionListener(this);
        FunctPanel.add(PreBtn);
        FunctPanel.add(LatBtn);
        FunctPanel.add(GuideText);
        FunctPanel.add(GoBtn);
        getContentPane().add(FunctPanel);
        
		//中部文件列表
        stack = new Stack<String>();
        stack_return = new Stack<String>();
        ShowPanel.setSize(300, 460);
        ShowPanel.setLocation(6, 120);
        
        ShowPanel.setLayout(null);    
        list = new JList<String>();
        jPopupMenu = new JPopupMenu();//文件/文件夹的属性菜单
        jPopupMenu2 = new JPopupMenu();//磁盘的属性菜单
        jPopupMenu3 = new JPopupMenu();//多选的属性菜单
        JMIs[0] = new JMenuItem("打开");
        JMIs[1] = new JMenuItem("删除");
        JMIs[2] = new JMenuItem("重命名");
        JMIs[3] = new JMenuItem("属性");
        JMIs[4] = new JMenuItem("解散");
        for(int k = 0; k < 5; ++k){//文件/文件夹的属性菜单初始化
        	JMIs[k].addActionListener(this);
        	jPopupMenu.add(JMIs[k]);            	
        }        
        JMIs2[0] = new JMenuItem("打开");
        JMIs2[1] = new JMenuItem("属性");
        for(int k = 0; k < 2; ++k){//磁盘的属性菜单初始化
        	JMIs2[k].addActionListener(this);
        	jPopupMenu2.add(JMIs2[k]);            	
        }    
        JMIs3[0] = new JMenuItem("删除");
        JMIs3[1] = new JMenuItem("整合文件");
        for(int k = 0; k < 2; ++k){//磁盘的属性菜单初始化
        	JMIs3[k].addActionListener(this);
        	jPopupMenu3.add(JMIs3[k]);            	
        } 
        list.add(jPopupMenu3);
        list.add(jPopupMenu2);
        list.add(jPopupMenu);
        
        Home_List();//显示磁盘根目录
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(list.getSelectedIndex() != -1){
					if(e.getClickCount() == 1){//单击list时，暂无事件
					
					}else if(e.getClickCount() == 2){//双击list时，打开文件或进入该子目录
						twoClick(list.getSelectedValue());												
					}
					if(e.getButton() == 3){//右击list时，打开菜单栏
						if(Cur_URL != ""){
							if(list.getSelectedValuesList().size() == 1){
								jPopupMenu.show(list,e.getX(),e.getY()); //如果右击的是单个文件夹和文件，则应打开一个功能齐全的菜单栏
							}else if(list.getSelectedValuesList().size() > 1){//如果选中多个文件夹和文件，则只支持删除功能
								jPopupMenu3.show(list, e.getX(), e.getY());
							}
						}		                 
						else if(Cur_URL == "" && list.getSelectedValuesList().size() == 1){
							jPopupMenu2.show(list, e.getX(), e.getY()); //如果右击的是磁盘，菜单栏中只含有“打开”和“属性”功能
						}						
					}
				}
			}
		});	
		ScrollShow = new JScrollPane(list);
		ShowPanel.add(ScrollShow);
		ScrollShow.setSize(300, 460);
		ScrollShow.setLocation(5, 5);
		getContentPane().add(ShowPanel);
		
		//左侧目录树状图
//        TreePanel.setSize(190,610);
//        TreePanel.setLocation(5, 90);
//       TreePanel.setLayout(null); 
//        filesTree = new FilesTree();
//        TreeShow = new JScrollPane(filesTree);
//        TreeShow.setBounds(5, 5, 185, 520);
//        TreePanel.add(TreeShow);
//        this.add(TreePanel);                   
        getContentPane().add(p);	
              
        // 创建选项卡面板
        final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(350, 90, 800, 490);
        
        // 创建第 1 个选项卡（选项卡只包含 标题）
        
        JPanel Rename = new JPanel(null);
        Rename.setBounds(10, 10, 600, 400);
        JLabel RenameType = new JLabel("添加方式:");
        RenameType.setBounds(10, 10, 100, 50);
        JLabel RenameValue = new JLabel("添加值:");
        RenameValue.setBounds(20, 70, 100, 50);
        BtnQian = new JRadioButton("前缀");
        BtnQian.setBounds(80, 10, 50, 50);
        BtnQian.setSelected(true);
        JRadioButton BtnHou = new JRadioButton("后缀");
        BtnHou.setBounds(150, 10, 50, 50);
        ButtonGroup RenameClass = new ButtonGroup();
        RenameClass.add(BtnQian);
        RenameClass.add(BtnHou);
        Revalue = new JTextField();
        SetName = new JButton("重命名");
        Revalue.setBounds(80, 80, 200, 30);
        SetName.addActionListener(this);
        SetName.setBounds(80,160,100,30);
        Rename.add(RenameValue);
        Rename.add(RenameType);
        Rename.add(BtnQian);
        Rename.add(BtnHou);
        Rename.add(Revalue);
        Rename.add(SetName);
        
        tabbedPane.addTab("重命名",Rename);
       
        // 创建第 2 个选项卡（选项卡包含 标题 和 图标）
        JPanel Classify = new JPanel(null);
        Classify.setBounds(10,10,600,400);
        JLabel ClassTypeName = new JLabel("分类方式:");
        ClassTypeName.setBounds(10,10,90,30);
        JLabel ClassPosition = new JLabel("目的地址:");
        ClassPosition.setBounds(10,60,90,30);
        PositionUrl = new JTextField();
        PositionUrl.setBounds(80,61,270,30);
        SearchDir = new JButton("浏览");
        SearchDir.setBounds(360,60,60,30);
		JCheckBox ClassiFyType1 = new JCheckBox("文件名");
		JCheckBox ClassiFyType2 = new JCheckBox("文件类型");
		JCheckBox ClassiFyType3 = new JCheckBox("时间");
		ClassiFyType1.setBounds(80,10,70,30);
		ClassiFyType2.setBounds(160,10,90,30);
		ClassiFyType3.setBounds(252,10,90,30);
		Classify.add(ClassTypeName);
		Classify.add(ClassiFyType1);
		Classify.add(ClassiFyType2);
		Classify.add(ClassiFyType3);
		Classify.add(ClassPosition);
		Classify.add(SearchDir);
		Classify.add(PositionUrl);
		SearchDir.addActionListener(this);
		tabbedPane.addTab("分类",Classify);
		
		JButton button = new JButton("分类");
		button.setBounds(80, 120, 93, 23);
		Classify.add(button);
        // 创建第 3 个选项卡（选项卡包含 标题、图标 和 tip提示）
        //tabbedPane.addTab("Tab03",); 
        
        getContentPane().add(tabbedPane);
        
        panel = new JPanel();
        tabbedPane.addTab("推荐操作", null, panel, null);
	}
	
	private void oneClick(String choice){//点击一次的方法
		
	}
	
	public void twoClick(String choice){//点击两次时的事件
		if(!isSearching){//如果此时不在搜索状态，就是正常的点击处理
			choice += "\\";	
			File file = new File(Cur_URL + choice);
			if(file.isDirectory()){
				Cur_URL += choice;	
				stack.push(Cur_URL);
				Go_There();
			}else{
				OpenIt(file);
			}
		}else{//如果是在搜索状态，那就要从map里提取我们的URL，因为搜索把顺序都打乱了，无法用一个URL对应
			File file = new File(Maps.get(choice));
			OpenIt(file);
		}
	}
	
	public void Home_List(){//回到初始磁盘界面
		List<String> Disks = MemoryInfo.getDisk();
		defaultListModel = new DefaultListModel();
		for(int i = 0; i < Disks.size(); ++i){
			defaultListModel.addElement(Disks.get(i));
		}
		Icon[] icons = GetFileIcon.getSmallIcon("HOME");//得到根目录下的图标
		list.setModel(defaultListModel);
		list.setCellRenderer(new MyCellRenderer(icons));
		GuideText.setText("");
		Cur_URL = "";
		stack.push(Cur_URL);
	}
	
	public void OpenIt(File file){//调用电脑中的程序“打开”文件的方法
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void Go_There(){//想去哪，就去哪（核心跳转函数）
			GuideText.setText(Cur_URL);
			PositionUrl.setText(Cur_URL);
			if(Cur_URL != ""){//Cur_URL非空，就跳入目标目录
				defaultListModel.clear();
				String[] getString = GetFileNames.getFileName(Cur_URL);		
				for(int i = 0; i < getString.length; ++i){
					defaultListModel.addElement(getString[i]);		
				}	
				Icon[] icons = GetFileIcon.getSmallIcon(Cur_URL);
				list.setModel(defaultListModel);
				list.setCellRenderer(new MyCellRenderer(icons));
				
			}else{//Cur_URL为空时，就跳转回根目录
				Home_List();
			}
	}
	
	public void GetAllResults(String path){//搜索功能核心函数
		  if(path != ""){		    	
				String[] getString = GetFileNames.getFileName(path);
				for(int i = 0; i < getString.length; ++i){
					File file = new File(path + getString[i] + "\\");						
					if(file.isDirectory()){//遍历子文件夹下						
						GetAllResults(path + getString[i] + "\\");
					}else{
						String prefix = getString[i].substring(getString[i].lastIndexOf('.') + 1);					
						if(VideoType.contains(prefix) && Videos.isSelected()){//判断是否为视频文件且视频按钮被选中，是则加入我们的显示目录里
							System.out.println(getString[i]);
							Maps.put(getString[i], path + getString[i]);//用Maps存储文件名与路径的对应关系
							defaultListModel.addElement(getString[i]);
							AllIcons[Icon_Counter++] = GetFileIcon.getSingleSmallIcon(path + getString[i]);
						}else if(GraphType.contains(prefix) && Picture.isSelected()){//判断是否为图片文件且图片按钮被选中，是则加入我们的显示目录里
							Maps.put(getString[i], path + getString[i]);//用Maps存储文件名与路径的对应关系
							defaultListModel.addElement(getString[i]);
							AllIcons[Icon_Counter++] = GetFileIcon.getSingleSmallIcon(path + getString[i]);
						}else if(TxtType.contains(prefix) && Text.isSelected()){//判断是否为文档文件且文档按钮被选中，是则加入我们的显示目录里
							Maps.put(getString[i], path + getString[i]);//用Maps存储文件名与路径的对应关系
							defaultListModel.addElement(getString[i]);
							AllIcons[Icon_Counter++] = GetFileIcon.getSingleSmallIcon(path + getString[i]);
						}else if(MusicType.contains(prefix) && Music.isSelected()){
							Maps.put(getString[i], path + getString[i]);//用Maps存储文件名与路径的对应关系
							defaultListModel.addElement(getString[i]);
							AllIcons[Icon_Counter++] = GetFileIcon.getSingleSmallIcon(path + getString[i]);
						}
					}				
				}
		    }
	}		
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        MainForm m = new MainForm();     
	}
	public boolean IsRenameValue(String RenameValue) {
		String[] UnValue = {"\\","/","?","*","<",">","\""};
		for(int i = 0;i<UnValue.length;i++) {
			if(RenameValue.indexOf(UnValue[i])!=-1)
				return true;
		}
		return false;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == PreBtn){//向左走
			LatURL = Cur_URL;			
			if(!stack.isEmpty()){
				stack.pop();//每从当前一个目录跳回之前的目录时，stack就要出栈		
				stack_return.push(Cur_URL);//把跳之前的目录放入返回栈stack_return
				if(!stack.isEmpty()){					
					Cur_URL = stack.peek();//从栈中得到上一个访问的目录，赋给当前目录					
				}
				else{
					Cur_URL = "";//如果栈为空，则说明前面是根目录，则直接置空
				}
				Go_There();
			}
			if(isSearching){//如果正在搜索状态，那此时应该结束
				isSearching = false;
				AllFiles.setSelected(true);
			}
		}
		
		else if(e.getSource() == LatBtn){//向右走
			if(!stack_return.isEmpty()){//向右走，则从返回栈里拿URL
				Cur_URL = stack_return.peek();
				stack_return.pop();
				stack.push(Cur_URL);
				Go_There();
			}
			if(isSearching){//如果正在搜索状态，那此时应该结束
				isSearching = false;
				AllFiles.setSelected(true);
			}
		}
		
		else if(e.getSource() == JMIs[0] || e.getSource() == JMIs2[0]){	//打开文件/文件夹/磁盘
			if(!isSearching){
				String url = Cur_URL + list.getSelectedValue();
			if(Cur_URL != ""){
				url += "\\";
			}
				File file = new File(url);
			if(file.isDirectory()){
				twoClick(url);
			}else{
				OpenIt(file);				
			}
			}else{
				File file = new File(Maps.get(list.getSelectedValue()));
				OpenIt(file);
			}			
		}
		
		else if(e.getSource() == JMIs[1]){//删除
			File file = new File(Cur_URL + "/" + list.getSelectedValue());
			int n;
			if(file.isFile()){
				n = JOptionPane.showConfirmDialog(null, "确定要删除文件 " + file.getName() + " 么?", "文件删除",JOptionPane.YES_NO_OPTION);
			}else{
				n = JOptionPane.showConfirmDialog(null, "确定要删除 " + file.getName() + " 及其目录下的文件么?", "文件夹删除",JOptionPane.YES_NO_OPTION);
			}
			if(n == 0){
				FileDelete.delete(Cur_URL + list.getSelectedValue() +  "\\");
				Go_There();
			}			
		}
		
		else if(e.getSource() == JMIs3[0]){//多选下的删除
			List<String> selected_str = list.getSelectedValuesList();
			File file;
			int num = selected_str.size();
			int n = JOptionPane.showConfirmDialog(null, "确定要删除 " + selected_str.get(0) + " 等" + num + "项么?", "文件删除",JOptionPane.YES_NO_OPTION);
			if(n == 0){
				if(isSearching){//如果是正在搜索，从Maps取URL
					for(int i = 0; i < selected_str.size(); ++i){
						file = new File(Maps.get(selected_str.get(i)));
						FileDelete.delete(file.getAbsolutePath());
					}				
				}else{//否则就用Cur_URL拼接获得
					for(int i = 0; i < selected_str.size(); ++i){
						FileDelete.delete(Cur_URL + selected_str.get(i) +  "\\");
					}		
					Go_There();
				}
			}						
		}
		else if(e.getSource() == JMIs3[1]) {//多选下的整合
			List<String> Addfiles = list.getSelectedValuesList();
			File file;
			SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间 
	        //sdf.applyPattern("yyyy-MM-dd_HH-mm-ss"); 
	        sdf.applyPattern("yyyy-MM-dd");
	        Date date = new Date();// 获取当前时间
	        String filenames = new String(Cur_URL+"整合文件夹"+sdf.format(date));
			file = new File(filenames);
			if(!file.exists()) {
				file.mkdir();
			}
			for(int i=0;i<Addfiles.size();i++) {
				file = new File(Cur_URL+Addfiles.get(i));
				File nowfile = new File(filenames+"\\"+Addfiles.get(i));
				if(nowfile.exists()) {
					for(int times=1;;times++){
						nowfile = new File(filenames+"\\"+Addfiles.get(i)+"-"+times);
						if(!nowfile.exists())
							break;
					}
				}
				file.renameTo(nowfile);
			}
			Go_There();
		}
		else if(e.getSource() == JMIs[4]) {//解散文件夹
			List<String> Addfiles = list.getSelectedValuesList();
			File file = new File(Cur_URL+Addfiles.get(0));
			if(file.isFile()){
				JOptionPane.showMessageDialog(null, "请选择文件夹","错误",JOptionPane.ERROR_MESSAGE);
			}
			else if(file.isDirectory()) {
				File[] files = file.listFiles();
				for(int i=0;i<files.length;i++) {
					file = files[i];
					File nowfile;
					String filename = files[i].toString().substring(files[i].toString().lastIndexOf("\\")+1, files[i].toString().length());
					nowfile = new File(Cur_URL+filename);
					if(nowfile.exists()) {
						for(int times=1;;times++){
							nowfile = new File(Cur_URL+filename+times);
							
									if(!nowfile.exists())
										break;
						}
					}
					file.renameTo(nowfile);
				}
				file = new File(Cur_URL+Addfiles.get(0));
				file.delete();
				file = new File(Cur_URL+Addfiles.get(0)+1);
				file.renameTo(new File(Cur_URL+Addfiles.get(0)));
				Go_There();
			}
		}
		else if(e.getSource() == JMIs[2]){//重命名
			String before = list.getSelectedValue();
			File file = new File(Cur_URL + before + "\\");
			String after = "";
			if(file.isDirectory()){
				after = (String) JOptionPane.showInputDialog(null, "请输入新文件夹名:\n", "重命名", JOptionPane.PLAIN_MESSAGE, null, null,
		                list.getSelectedValue());
			}else{
				after = (String) JOptionPane.showInputDialog(null, "请输入新文件名:\n", "重命名", JOptionPane.PLAIN_MESSAGE, null, null,
		                list.getSelectedValue());
			}			
			if(before != after && after != null){
				new File(Cur_URL + before + "\\").renameTo(new File(Cur_URL + after + "\\"));
				Go_There();
			}else{
				Go_There();
			}
		}
		
		else if(e.getSource() == JMIs[3]){//打开文件/文件夹属性窗口
			String temp_url = Cur_URL + list.getSelectedValue() + "\\";
			File file = new File(temp_url);
			Icon icon = GetFileIcon.getSingleSmallIcon(temp_url);			
			String name = list.getSelectedValue();
			long size;
			double final_size;
		    long File_Num = 0, Directory_Num = 0;
		    int flag = 0;
			String file_size = "";
			
			String Create_Time = FileTime.getCreateTime(temp_url);
			String Modify_Time = FileTime.getModifiedTime(temp_url);
			String Last_Access = FileTime.getLatestAccessTime(temp_url);
			
			if(file.isDirectory()){//目录属性初始化所需参数
				DirectoryInfo DInfo = new DirectoryInfo();
				size = DInfo._instance.getDirSize(file);
				File_Num = DInfo.File_Num;
				Directory_Num = DInfo.Directory_Num;
				flag = 1;
			}else{//文件属性初始化所需参数
				size = file.length();				
			}			 
			final_size = 0;				
			for(int i = 0; i < Sizes.length; ++i){
				if(size / Sizes[i] > 0){
					final_size = size * 1.0 / Sizes[i];
					DecimalFormat fnum = new DecimalFormat("##0.00");  
					file_size = fnum.format(final_size) + Size_Names[i];
					break;
				}
			}
			if(flag == 1){
				FileProperties properties = new FileProperties(icon, name, file_size, File_Num, Directory_Num-1, temp_url, Create_Time);
			}else{
				FileProperties properties = new FileProperties(icon, name, file_size, temp_url, Create_Time, Modify_Time, Last_Access);
			}		
		}
		
		else if(e.getSource() == JMIs2[1]){//磁盘属性查看
			String temp_url = list.getSelectedValue() + "\\";
			Icon icon = GetFileIcon.getSingleSmallIcon(temp_url);	
			File file = new File(temp_url);			
			FileSystemView fileSys=FileSystemView.getFileSystemView();
			String name = fileSys.getSystemDisplayName(file);
			double Available = file.getFreeSpace() * 1.0 / Sizes[0];		
			double Used = file.getTotalSpace()* 1.0 / Sizes[0] - Available;
			FileProperties properties = new FileProperties(icon, name, Used, Available);
		}
		
		else if(e.getSource() == GoBtn || e.getSource() == GuideText){//通过地址栏进行文件地址跳转
			String url = GuideText.getText();
			if(url.length() > 0){
			File file = new File(url);
			if(file.exists()){
				stack.push(Cur_URL);
				Cur_URL = url;
				Go_There();
			}else{
				JOptionPane.showConfirmDialog(null, "没有找到该目录", "确认对话框", JOptionPane.YES_OPTION);
			}
			}else{
				Home_List();
			}
		}
		
		else if(e.getSource() == AllFiles){//如果是搜索完切换回显示所有文件，则回到主目录
			isSearching = false;		
			Home_List();
		}
		
		else if(e.getSource() == Videos || e.getSource() == Picture || e.getSource() == Text || e.getSource() == Music){//如果选择了搜索功能
			 isSearching = true;
			 Maps.clear();
			 isSearching = true;
			 defaultListModel.clear();
			 Icon_Counter = 0;
			 AllIcons = new Icon[999999];
			 GetAllResults(Cur_URL);
			 list.setModel(defaultListModel);
			 list.setCellRenderer(new MyCellRenderer(AllIcons));
		}	
		
		else if(e.getSource() == SearchText){//搜索框输入后按回车键触发该事件
			 boolean flag_Dir = false, flag_File = false;
			 if(FileCheck.isSelected()){//搜索文件名
				 flag_File = true;
			 }
			 if(DirCheck.isSelected()){//搜索文件夹名
				 flag_Dir = true;
			 }
			 if(!(flag_File || flag_Dir)){//两个都不选是不行的
				 JOptionPane.showMessageDialog(null, "请至少选择一个搜索类别", "确认对话框", JOptionPane.YES_OPTION);
			 }else{//开始搜索
				 isSearching = true;
				 Maps.clear();
				 isSearching = true;
				 defaultListModel.clear();
				 Icon_Counter = 0;
				 AllIcons = new Icon[999999];
				 FileSearch.bfsSearchFile(Cur_URL, SearchText.getText(), flag_Dir, flag_File);
				 list.setModel(defaultListModel);
				 list.setCellRenderer(new MyCellRenderer(AllIcons));
			 }
		}
		else if (e.getSource() == SearchDir) {//选择目录
			JFileChooser FileChoser=new JFileChooser(Cur_URL);                        
			FileChoser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
			FileChoser.showDialog(new JLabel(), "选择");
		    File file=FileChoser.getSelectedFile();
		    System.out.println(file); 
		    if(file.isDirectory()){
		    	PositionUrl.setText(file.getAbsolutePath());
	        System.out.println("文件夹:"+file.getAbsolutePath()); 
		    }              
		    System.out.println(FileChoser.getSelectedFile().getName());        
		}
		else if (e.getSource() == SetName) {
			Object[] RenameList = list.getSelectedValuesList().toArray();
			String RenameValue = Revalue.getText();
			boolean Error= false;
			if(IsRenameValue(RenameValue)) {
				JOptionPane.showMessageDialog(null, "文件夹在命名的时候不能包含下列字符：\r\n【\\ 】【 / 】 【: 】【*】【？】【<】【> 】【| 】【\"】","输入错误",JOptionPane.ERROR_MESSAGE);
				Error = true;
			}
			if(!Error) {
				if(BtnQian.isSelected()) {
					for(int i=0;i<RenameList.length;i++) {
						File FileData = new File(Cur_URL+RenameList[i]);
						FileData.renameTo(new File(Cur_URL+RenameValue+RenameList[i]));				
					}
				}else {
					for(int i=0;i<RenameList.length;i++) {
						String Listfile = RenameList[i].toString();
						String fileTyle = new String();
						String fileName = new String();
						if(Listfile.lastIndexOf(".")!=-1) {
							fileTyle = Listfile.substring(Listfile.lastIndexOf("."),Listfile.length());		
							fileName = Listfile.substring(0,Listfile.lastIndexOf("."));
						}else {
							fileTyle = "";
							fileName = Listfile;
						}					
						File FileData = new File(Cur_URL+RenameList[i]);
						FileData.renameTo(new File(Cur_URL+fileName+RenameValue+fileTyle));	
						System.out.println(fileName+RenameValue+fileTyle+"\n");					
					}
				}
		    Go_There();
			}
		}
		else if(e.getActionCommand().equals("comboBoxChanged")) {
			String s1 = (String)SortList.getSelectedItem();
			String s2 = (String)SortType.getSelectedItem();
			//System.out.println(s1+" "+s2);
			if(s2.equals("升序")) {
				if(s1.equals("修改时间")) {
					defaultListModel.clear();
					File file = new File(Cur_URL);
			        File[] files = file.listFiles();
			        Arrays.sort(files, new Comparator<File>() {
			            public int compare(File f1, File f2) {
			                long diff = f1.lastModified() - f2.lastModified();
			                if (diff > 0)
			                    return 1;
			                else if (diff == 0)
			                    return 0;
			                else
			                    return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
			            }

			            public boolean equals(Object obj) {
			                return true;
			            }

			        });
					for(int i = 0;i < files.length;i++) {
						defaultListModel.addElement(files[i].getName());
						//System.out.println("files[i].getName()");
					}
					Icon[] icons = GetFileIcon.getSmallIcon2(Cur_URL,files);
					list.setModel(defaultListModel);
					list.setCellRenderer(new MyCellRenderer(icons));
				}
				else if(s1.equals("首字母")) {
					defaultListModel.clear();
					File file = new File(Cur_URL);
					File[] files = file.listFiles();
					Arrays.sort(files,new Comparator<File>() {
						public int compare(File f1,File f2) {
							String s1 = f1.getName().toLowerCase();
							String s2 = f2.getName().toLowerCase();
							int result = s1.compareTo(s2);
							if(result == 0)
								return 0;
							else if(result == 1)
								return -1;
							else 
								return 1;
						}
					});
					for(int i = 0;i < files.length;i++) {
						defaultListModel.addElement(files[i].getName());
					}
					Icon[] icons = GetFileIcon.getSmallIcon2(Cur_URL,files);
					list.setModel(defaultListModel);
					list.setCellRenderer(new MyCellRenderer(icons));
				}
				else if(s1.equals("文件大小")) {
					File file = new File(Cur_URL);
					File[] files = file.listFiles();
					defaultListModel.clear();
					if(files != null) {
						List<File> fileList = Arrays.asList(files);
						Collections.sort(fileList, new Comparator<File>() {
							public int compare(File f1,File f2) {
								if(f1.isDirectory() && !f2.isDirectory())
									return -1;
								else if(!f1.isDirectory() && f2.isDirectory())
									return 1;
								else {
									long num1 = f1.length();
									long num2 = f2.length();
									if(num1 > num2) return 1;
									else if(num1 == num2) return 0;
									else return -1;
								}
							}
						});
						for(int i = 0;i < files.length;i++) {
							defaultListModel.addElement(files[i].getName());
						}
						Icon[] icons = GetFileIcon.getSmallIcon2(Cur_URL,files);
						list.setModel(defaultListModel);
						list.setCellRenderer(new MyCellRenderer(icons));
				}
			}
			}
			if(s2.equals("降序")) {
				if(s1.equals("修改时间")) {
					defaultListModel.clear();
					File file = new File(Cur_URL);
			        File[] files = file.listFiles();
			        Arrays.sort(files, new Comparator<File>() {
			            public int compare(File f1, File f2) {
			                long diff = f1.lastModified() - f2.lastModified();
			                if (diff > 0)
			                    return -1;
			                else if (diff == 0)
			                    return 0;
			                else
			                    return 1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
			            }

			            public boolean equals(Object obj) {
			                return true;
			            }

			        });
					for(int i = 0;i < files.length;i++) {
						defaultListModel.addElement(files[i].getName());
						//System.out.println("files[i].getName()");
					}
					Icon[] icons = GetFileIcon.getSmallIcon2(Cur_URL,files);
					list.setModel(defaultListModel);
					list.setCellRenderer(new MyCellRenderer(icons));
				}
				else if(s1.equals("首字母")) {
					defaultListModel.clear();
					File file = new File(Cur_URL);
					File[] files = file.listFiles();
					Arrays.sort(files,new Comparator<File>() {
						public int compare(File f1,File f2) {
							String s1 = f1.getName().toLowerCase();
							String s2 = f2.getName().toLowerCase();
							int result = s1.compareTo(s2);
							if(result == 0)
								return 0;
							else if(result == 1)
								return 1;
							else 
								return -1;
						}
					});
					for(int i = 0;i < files.length;i++) {
						defaultListModel.addElement(files[i].getName());
					}
					Icon[] icons = GetFileIcon.getSmallIcon2(Cur_URL,files);
					list.setModel(defaultListModel);
					list.setCellRenderer(new MyCellRenderer(icons));
				}
				else if(s1.equals("文件大小")) {
					File file = new File(Cur_URL);
					File[] files = file.listFiles();
					defaultListModel.clear();
					if(files != null) {
						List<File> fileList = Arrays.asList(files);
						Collections.sort(fileList, new Comparator<File>() {
							public int compare(File f1,File f2) {
								if(f1.isDirectory() && !f2.isDirectory())
									return 1;
								else if(!f1.isDirectory() && f2.isDirectory())
									return -1;
								else {
									long num1 = f1.length();
									long num2 = f2.length();
									if(num1 > num2) return -1;
									else if(num1 == num2) return 0;
									else return 1;
								}
							}
						});
						for(int i = 0;i < files.length;i++) {
							defaultListModel.addElement(files[i].getName());
						}
						Icon[] icons = GetFileIcon.getSmallIcon2(Cur_URL,files);
						list.setModel(defaultListModel);
						list.setCellRenderer(new MyCellRenderer(icons));
					}
				}
			}
			
		}
	}
}

