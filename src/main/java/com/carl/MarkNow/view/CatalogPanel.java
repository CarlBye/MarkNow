package com.carl.MarkNow.view;

import java.awt.BorderLayout;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.carl.MarkNow.model.CatalogNode;
import com.carl.MarkNow.model.Text;

public class CatalogPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Text text;
	private JTree catalogTree;
	private DefaultMutableTreeNode rootNode;
	private JScrollPane scrollPane;

	public CatalogPanel(Text t) {
		text = t;
		text.setCatalogPanel(this);
		setLayout(new BorderLayout());
		
		rootNode = new DefaultMutableTreeNode(new CatalogNode(0, -1, "文档目录"));
		catalogTree = new JTree(rootNode);
		catalogTree.setRootVisible(true);
//this path starts from root of the project		
		Icon openIcon = new ImageIcon("src/image/open.png");
		Icon closedIcon = new ImageIcon("src/image/closed.png");
		Icon leafIcon = new ImageIcon("src/image/leaf.png");
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setClosedIcon(closedIcon);
		renderer.setOpenIcon(openIcon);
		renderer.setLeafIcon(leafIcon);
		catalogTree.setCellRenderer(renderer);
		
		catalogTree.addTreeSelectionListener( new TreeSelectionListener() {		
			
			public void valueChanged(TreeSelectionEvent e) {				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) catalogTree.getLastSelectedPathComponent();
				if (node == null) {
					return;
				}
				else {
					CatalogNode catalogNode = (CatalogNode) node.getUserObject();
					JScrollPane scrollPane = text.getEditPanel().getScrollPane();
					JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
					scrollBar.setValue(15 * catalogNode.getLineNO());
				}
			}
			
		} );
		
		scrollPane = new JScrollPane(catalogTree);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void updateTree() {
		String[] lines = text.getTextContent().split("\\n");
		rootNode.removeAllChildren();
		DefaultMutableTreeNode parentNode = rootNode;
		int parentLevel = 0;
		for (int i = 0; i < lines.length; i++) {
			DefaultMutableTreeNode currentTreeNode = createTreeNode(i+1, lines[i]);
			if (currentTreeNode != null) {
				int currentLevel = ((CatalogNode) (currentTreeNode.getUserObject())).getLevel();
				if (currentLevel > parentLevel) {
					parentNode.add(currentTreeNode);
					currentTreeNode.setParent(parentNode);
				}
				else {
					while (true) {
						parentNode = (DefaultMutableTreeNode) parentNode.getParent();
						parentLevel = ((CatalogNode) (parentNode.getUserObject())).getLevel();
						if (currentLevel > parentLevel) {
							parentNode.add(currentTreeNode);
							currentTreeNode.setParent(parentNode);
							break;
							}
						}
					}
				parentNode = currentTreeNode;
				parentLevel = currentLevel;
			}
		}
		catalogTree.updateUI();
		expandAll(catalogTree, new TreePath(rootNode), true);
	}

    private DefaultMutableTreeNode createTreeNode(int lineNO, String line) {
        Pattern pattern = Pattern.compile("^(#+)(.+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            int level = matcher.group(1).length();
            String title = matcher.group(2);
            return new DefaultMutableTreeNode(new CatalogNode(level, lineNO, title));
        } else {
            return null;
        }
    }

    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
