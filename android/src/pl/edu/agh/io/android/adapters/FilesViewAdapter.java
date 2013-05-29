package pl.edu.agh.io.android.adapters;

/**
 * Created with IntelliJ IDEA.
 * User: mjjaniec
 * Date: 4/28/13
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import pl.edu.agh.io.android.activities.R;
import pl.edu.agh.io.android.model.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class FilesViewAdapter extends ArrayAdapter<FileItem> {
    private final Context context;
    private final List<FileItem> values;

    private String root;

    public static interface OnItemClick {
        void onItemClick(FileItem fileItem, boolean picked);
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    public static FilesViewAdapter createInstanced(Context context) {
        List<FileItem> list = new ArrayList<FileItem>();
        return new FilesViewAdapter(context, list);

    }

    private FilesViewAdapter(Context context, List<FileItem> values) {
        super(context, R.layout.row_filesview, values);

        this.context = context;
        this.values = values;

        File ext = Environment.getExternalStorageDirectory();
        if (ext.listFiles() != null) {
            root = ext.getAbsolutePath();
        } else {
            root = Environment.getDataDirectory().getAbsolutePath();
        }
        getDir(root);
    }


    private void getDir(String dirPath) {
        values.clear();
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (!dirPath.equals(root)) {
            values.add(new FileItem("../", f.getParent(), true));
        }

        Arrays.sort(files, filecomparator);

        for (File file : files) {
            if (!file.isHidden() && file.canRead()) {
                if (file.isDirectory()) {
                    values.add(new FileItem(file.getName() + "/", file.getPath(), true));
                } else {
                    values.add(new FileItem(file.getName(), file.getPath(), false));
                }
            }
        }

        notifyDataSetChanged();
    }

    /**
     * Directories first in alphabetical order then files in the same order.
     */
    private Comparator<? super File> filecomparator = new Comparator<File>() {

        public int compare(File file1, File file2) {

            if (file1.isDirectory()) {
                if (file2.isDirectory()) {
                    return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
                } else {
                    return -1;
                }
            } else {
                if (file2.isDirectory()) {
                    return 1;
                } else {
                    return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
                }
            }

        }
    };

    class FilesViewOnClickListener implements View.OnClickListener {

        private int position;

        public FilesViewOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            FileItem fileItem = values.get(position);
            File file = new File(fileItem.getPath());

            if (file.isDirectory()) {
                getDir(fileItem.getPath());
            } else {
                if (onItemClick != null)
                    onItemClick.onItemClick(fileItem, !fileItem.isDirectory());
            }
        }
    }

    private int getIconId(FileItem fileItem) {
        if (fileItem.isDirectory())
            return R.drawable.folder;

        if (fileItem.isImage())
            return R.drawable.picture;

        if (fileItem.isSound())
            return R.drawable.note;

        return R.drawable.file;
    }

    private int getButtonCaptionId(FileItem fileItem) {
        if (fileItem.isDirectory()) {
            return R.string.choosefile__open;
        } else {
            return R.string.choosefile__pick;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_filesview, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.filesview__name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.filesview__image);
        Button button = (Button) rowView.findViewById(R.id.filesview__button);

        FileItem fileItem = values.get(position);

        textView.setText(fileItem.toString());
        button.setText(getButtonCaptionId(fileItem));
        imageView.setImageResource(getIconId(fileItem));
        button.setOnClickListener(new FilesViewOnClickListener(position));

        return rowView;
    }
}
