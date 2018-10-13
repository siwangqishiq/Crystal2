package com.xinlan.crystal.role;

import java.util.HashSet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.xinlan.crystal.Resource;
import com.xinlan.crystal.role.particle.BombParticle;
import com.xinlan.crystal.screen.GameScreen;

public final class CoreData
{
    public static int PAD = 15;// 边距值
    public static int CUBE = 75;// 色块大小

    public static final int TYPE_NUM = 4;// 类型数量
    public static final int BLUE = 1;// 兰
    public static final int RED = 2;// 红
    public static final int YELLOW = 3;// 黄
    public static final int PINK = 4;// 粉
    public static final int BOMB = 5;// 炸弹

    public static final int CAN_DROP = -7;// 可下落标志点

    public static final int CUBE_WIDTH = 75;// 宽度
    public static final int CUBE_HEIGHT = 60;// 高度
    public static final int CUBE_BORN_Y = 150;

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_GROWING = 2;
    public static final int STATUS_DROPING = 3;// 下落调整状态

    public int status = STATUS_NORMAL;

    public static final int rowNum = 10;// 行数
    public static final int colNum = 6;// 列数

    public static final int SEED = 100;

    private GameScreen context;
    public TextureRegion blueTexture;
    public TextureRegion redTexture;
    public TextureRegion yellowTexture;
    public TextureRegion pinkTexture;
    public Sprite gameOverSprite;// 游戏结束 标示

    public boolean isDead = false;// 游戏状态是否死亡

    private final Pool<Pos> pointPool = new Pool<Pos>(100, 200)
    {
        @Override
        protected Pos newObject()
        {
            return new Pos();
        }
    };

    private Array<Pos> pathPoint = new Array<Pos>();// 计算路径值 存贮容器
    private HashSet<Integer> recordVistPointSet = new HashSet<Integer>();// 记录访问过得节点

    public static float Dump_Grow_Span = 6f;// 产生方块毫秒间隔
    // public static float Dump_Grow_Span = 0.1f;// 产生方块毫秒间隔

    private float growDy = 6;
    private float growY = 0;
    private float growDx = 5;
    private float growX = 0;

    // public int[][] data = {// 主运算矩阵
    // { 1, 1, 1, 1, 1, 1 },//
    // { 1, 1, 1, 1, 1, 1 },//
    // { 1, 1, 1, 1, 1, 1 },//
    // { 2, 2, 2, 0, 2, 2 },//
    // { 2, 2, 2, 0, 2, 2 }, //
    // { 1, 1, 0, 0, 0, 0 },//
    // { 0, 0, 0, 0, 0, 0 },//
    // { 0, 0, 0, 0, 0, 0 },//
    // { 0, 0, 0, 0, 0, 0 },//
    // { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };

    public int[][] data = {// 主运算矩阵
    { 0, 0, 0, 0, 0, 0 },// 1
            { 0, 0, 0, 0, 0, 0 },// 2
            { 0, 0, 0, 0, 0, 0 },// 3
            { 0, 0, 0, 0, 0, 0 },// 4
            { 0, 0, 0, 0, 0, 0 }, // 5
            { 0, 0, 0, 0, 0, 0 },// 6
            { 0, 0, 0, 0, 0, 0 },// 7
            { 0, 0, 0, 0, 0, 0 },// 8
            { 0, 0, 0, 0, 0, 0 },// 9
            { 0, 0, 0, 0, 0, 0 } };// 10

    private int[][] tempData1 = new int[rowNum][colNum];// 临时数据存贮1
    private int[][] canDropData = new int[rowNum][colNum];// 临时数据存贮2

    private int[] temp = new int[colNum];// 临时存贮单行数组变量
    private float countTime = 0;// 计数器

    private int dropFrameNum = 6;
    private int dropFrameIndex = 0;
    private int[] dropArray = new int[dropFrameNum];

    private BombParticle bombParticle;// 消失粒子效果

    private float xx;// debug

    public CoreData(GameScreen context)
    {
        this.context = context;

        bombParticle = new BombParticle(context);

        blueTexture = Resource.getInstance().blueTextureRegion;
        redTexture = Resource.getInstance().redTextureRegion;
        yellowTexture = Resource.getInstance().yellowTextureRegion;
        pinkTexture = Resource.getInstance().pinkTextureRegion;

        gameOverSprite = Resource.getInstance().gameOverSprite;

        restart();
    }

    public void restart()
    {
        isDead = false;
        for (int i = 0; i < rowNum; i++)
            for (int j = 0; j < colNum; j++)
                data[i][j] = 0;
        
        status = STATUS_NORMAL;
        int dropDelta = CUBE_HEIGHT / dropFrameNum;
        for (int i = 0; i < dropFrameNum; i++)
        {
            if (i == 0)
            {
                dropArray[i] = 0;
            }
            else
            {
                dropArray[i] = dropArray[i - 1] + dropDelta;
            }
        }// end for i
        countTime = 0;// 计数器
        genBottomOneRow(false);
    }

    public void genBottomOneRow(boolean isSound)
    {
        // 判断最后一行是否有值 游戏结束判断
        // isDead = false;
        checkIsDead();

        for (int i = 0; i < colNum; i++)
        {
            temp[i] = MathUtils.random(1, TYPE_NUM);
        }// end for i

        for (int j = rowNum - 1; j >= 1; j--)
        {
            System.arraycopy(data[j - 1], 0, data[j], 0, colNum);
        }// end for

        System.arraycopy(temp, 0, data[0], 0, colNum);
        status = STATUS_GROWING;
        if (isSound)
            context.gameSound.playGenerateSound();
    }

    /**
     * 检查是否死亡
     */
    private void checkIsDead()
    {
        for (int i = 0; i < colNum; i++)
        {
            if (data[rowNum - 1][i] != 0)
            {
                System.out.println("游戏结束");
                isDead = true;
                context.mGameOver.reInit(true);
                break;
            }
        }// end for i
    }

    /**
     * 绘制下落态的矩阵
     * 
     * @param batch
     */
    private void showDataDropping(SpriteBatch batch, int[][] matrix,
            int[][] canDropMatrix)
    {
        // System.out.println("xxxxxxxxxxx");
        int startX = PAD;
        int startY = GameScreen.SC_HEIGHT - CUBE_BORN_Y;
        for (int i = 0; i < rowNum; i++)
        {
            for (int j = 0; j < colNum; j++)
            {
                if (canDropMatrix[i][j] != CAN_DROP)// 非可下落点 正常绘制
                {
                    drawCube(batch, matrix[i][j], startX, startY, CUBE_WIDTH,
                            CUBE_HEIGHT);
                }
                else
                {// 可下落点 加上偏移量
                    int offset = dropArray[dropFrameIndex];
                    drawCube(batch, matrix[i][j], startX, startY + offset,
                            CUBE_WIDTH, CUBE_HEIGHT);
                }
                startX += CUBE_WIDTH;
            }// end for j
            startX = PAD;
            startY -= CUBE_HEIGHT;
        }// end for i
    }

    private void showDataNormal(SpriteBatch batch)
    {
        int startX = PAD;
        int startY = GameScreen.SC_HEIGHT - CUBE_BORN_Y;
        for (int i = 0; i < rowNum; i++)
        {
            for (int j = 0; j < colNum; j++)
            {
                drawCube(batch, data[i][j], startX, startY, CUBE_WIDTH,
                        CUBE_HEIGHT);
                startX += CUBE_WIDTH;
            }// end for j
            startX = PAD;
            startY -= CUBE_HEIGHT;
        }// end for i
    }

    private void showDataGrowing(SpriteBatch batch)
    {
        int startX = PAD;
        int startY = GameScreen.SC_HEIGHT - CUBE_BORN_Y;
        for (int i = 0; i < rowNum; i++)
        {
            if (i == 0)
            {
                float drawY = startY + CUBE_HEIGHT - growY;
                for (int j = 0; j < colNum; j++)
                {
                    float drawX = startX + CUBE_WIDTH / 2 - growX;
                    drawCube(batch, data[i][j], drawX, drawY, 2 * growX, growY);
                    startX += CUBE_WIDTH;
                }// end for j
                startX = PAD;
                startY -= growY;
            }
            else
            {
                for (int j = 0; j < colNum; j++)
                {
                    drawCube(batch, data[i][j], startX, startY, CUBE_WIDTH,
                            CUBE_HEIGHT);
                    startX += CUBE_WIDTH;
                }// end for j
                startX = PAD;
                startY -= CUBE_HEIGHT;
            }
        }// end for i
    }

    /**
     * 绘制方块
     * 
     * @param batch
     * @param type
     * @param startX
     * @param startY
     * @param width
     * @param height
     */
    private void drawCube(SpriteBatch batch, int type, float startX,
            float startY, float width, float height)
    {
        switch (type)
        {
            case BLUE:
                batch.draw(blueTexture, startX, startY, width, height);
                break;
            case RED:
                batch.draw(redTexture, startX, startY, width, height);
                break;
            case YELLOW:
                batch.draw(yellowTexture, startX, startY, width, height);
                break;
            case PINK:
                batch.draw(pinkTexture, startX, startY, width, height);
                break;
        }
    }

    /**
     * 游戏结束时 所绘制页面
     * 
     * @param batch
     * @param delta
     */
    public void gameOverDraw(SpriteBatch batch, float delta)
    {
        showDataNormal(batch);
    }

    public void draw(SpriteBatch batch, float delta)
    {
        // System.out.println("---->"+status);
        switch (status)
        {
            case STATUS_NORMAL:// 正常状态
                showDataNormal(batch);
                countTime += delta;
                if (countTime >= Dump_Grow_Span
                        && context.addDump.status != AddDump.STATUS_SHOOTING)// 产生新的一行
                {
                    countTime = 0;
                    genBottomOneRow(true);
                }
                break;
            case STATUS_GROWING:// 增长状态
                showDataGrowing(batch);
                if (growY >= CUBE_HEIGHT)
                {
                    growY = 0;
                    status = STATUS_NORMAL;
                    growX = 0;
                }
                else
                {
                    growY += growDy;
                    if (growX < CUBE_WIDTH >> 1)
                    {
                        growX += growDx;
                    }
                    else
                    {
                        growX = CUBE_WIDTH >> 1;
                    }
                }
                // checkIsDead();//检查是否死亡
                break;
            case STATUS_DROPING:// 下落调整状态
                if (dropFrameIndex < dropArray.length)
                {
                    showDataDropping(batch, tempData1, canDropData);// 显示主屏幕
                    dropFrameIndex++;
                }
                else
                {
                    dropFrameIndex = 0;
                    if (compareMatrix(tempData1, data))
                    {// 调整完成
                        status = STATUS_NORMAL;
                    }
                    else
                    {
                        adjusMatrixOneStep(tempData1);
                        setCanDropMatrix(tempData1, canDropData);
                    }
                    showDataDropping(batch, tempData1, canDropData);// 显示主屏幕
                }
                break;
        }// end switch

        bombParticle.draw(batch, delta);// 更新粒子系统
    }

    /**
     * 根据指定行 计算出当前行的最大y坐标值
     * 
     * @param col
     * @return
     */
    public int canStayPosYFromCol(int col)
    {
        int layer = 0;
        for (int i = rowNum - 1; i >= 0; i--)
        {
            if (data[i][col] != 0)
            {// 不为空
                layer = i + 1;
                break;
            }
        }// end for i

        if (layer >= rowNum - 1)
        {
            isDead = true;
            context.mGameOver.reInit(true);
        }

        context.addDump.nextRowValue = layer;
        return GameScreen.SC_HEIGHT - CUBE_BORN_Y - layer * CUBE_HEIGHT;
    }

    /**
     * 更新矩阵 计算联通路径
     */
    public void updateMatrix(int pointRow, int pointCol)
    {
        clearPathPoint();// 清空原有数据
        int value = data[pointRow][pointCol];// 取出触发点的值
        if (value == CoreData.BOMB)// 新增加的点是炸弹
        {
            calBombDamage(pointRow, pointCol);
            bombUpdate();
        }
        else
        {// 普通团子点
            Pos point = pointPool.obtain();
            point.row = pointRow;// 记录行值
            point.col = pointCol;// 记录列值
            pathPoint.add(point);// 记录第一个点

            int setValue = pointRow * SEED + pointCol;
            recordVistPointSet.add(setValue);// 将更新点 加入记录列表中
            calPath(pointRow, pointCol, value);// 开始递归搜索联通路径

            // System.out.println(pathPoint.size);
            normalUpdate();
        }
    }

    private void bombUpdate()
    {
        for (int i = 0, size = pathPoint.size; i < size; i++)
        {
            Pos pos = pathPoint.get(i);
            bombParticle.addParticle(data[pos.row][pos.col], pos.row, pos.col);// 加入爆炸粒子效果
            data[pos.row][pos.col] = 0;
        }// end for i
         // 统计出需要调整的节点
        copyMatrix(data, tempData1);// 拷贝原始主矩阵副本
        adjustMainMatrix();// 更新主矩阵 使其变成下落完成状态
        // adjusMatrixOneStep(tempData1);
        setCanDropMatrix(tempData1, canDropData);
        this.status = STATUS_DROPING;// 进入调整矩阵状态

        context.score.addScore(pathPoint.size);// 增加分数

        context.gameSound.playKillSound();
    }

    private void normalUpdate()
    {
        int num = pathPoint.size;
        if (num >= 3)// 大于3个处于联通状态的
        {
            // 更新
            for (int i = 0, size = pathPoint.size; i < size; i++)
            {
                Pos pos = pathPoint.get(i);
                bombParticle.addParticle(data[pos.row][pos.col], pos.row,
                        pos.col);// 加入爆炸粒子效果
                data[pos.row][pos.col] = 0;
            }// end for i
             // 统计出需要调整的节点
            copyMatrix(data, tempData1);// 拷贝原始主矩阵副本
            adjustMainMatrix();// 更新主矩阵 使其变成下落完成状态
            // adjusMatrixOneStep(tempData1);
            setCanDropMatrix(tempData1, canDropData);

            this.status = STATUS_DROPING;// 进入调整矩阵状态

            context.gameSound.playKillSound();

            context.score.addScore(num);// 更新分数
        }
    }

    /**
     * 计算爆炸影响点 1 1 1 1 1 1 0 1 1 1 1 1 1 1 1
     */
    private void calBombDamage(int row, int col)
    {
        int originRow = row;
        int originCol = col;
        if (col <= 0 && row <= 0)// 左上角
        {
            addPointToPath(row, col + 1);
            addPointToPath(row + 1, col);
            addPointToPath(row + 1, col + 1);
        }
        else if (col + 1 >= colNum && row <= 0)// 右上角
        {
            addPointToPath(row, col - 1);
            addPointToPath(row + 1, col - 1);
            addPointToPath(row + 1, col);
        }
        else if (row <= 0 && col + 1 < colNum && col > 0)// 上界
        {
            addPointToPath(row, col - 1);
            addPointToPath(row, col + 1);
            addPointToPath(row + 1, col - 1);
            addPointToPath(row + 1, col);
            addPointToPath(row + 1, col + 1);
        }
        else if (row + 1 >= rowNum && col + 1 >= colNum)// 右下角
        {
            addPointToPath(row - 1, col - 1);
            addPointToPath(row - 1, col);
            addPointToPath(row, col - 1);
        }
        else if (row + 1 < rowNum && row > 0 && col + 1 >= colNum)// 右边
        {
            System.out.println("5");
            addPointToPath(row - 1, col - 1);
            addPointToPath(row - 1, col);
            addPointToPath(row, col - 1);
            addPointToPath(row + 1, col - 1);
            addPointToPath(row + 1, col);
        }
        else if (row + 1 >= rowNum && col > 0 && col + 1 < colNum)// 下边
        {
            addPointToPath(row - 1, col - 1);
            addPointToPath(row - 1, col);
            addPointToPath(row - 1, col + 1);
            addPointToPath(row, col - 1);
            addPointToPath(row, col + 1);
        }
        else if (row + 1 >= rowNum && col <= 0)// 左下角
        {
            addPointToPath(row - 1, col);
            addPointToPath(row - 1, col + 1);
            addPointToPath(row, col + 1);
        }
        else if (row > 0 && row + 1 < rowNum && col <= 0)// 左边
        {
            addPointToPath(row - 1, col);
            addPointToPath(row - 1, col + 1);
            addPointToPath(row, col + 1);
            addPointToPath(row + 1, col);
            addPointToPath(row + 1, col + 1);
        }
        else if (row > 0 && row + 1 < rowNum && col > 0 && col + 1 < colNum)// 中间情况
        {
            addPointToPath(row - 1, col - 1);
            addPointToPath(row - 1, col);
            addPointToPath(row - 1, col + 1);
            addPointToPath(row, col - 1);
            addPointToPath(row, col + 1);
            addPointToPath(row + 1, col - 1);
            addPointToPath(row + 1, col);
            addPointToPath(row + 1, col + 1);
        }

        this.data[originRow][originCol] = 0;// 清空原有点

        context.gameSound.playBombSound();
    }

    private void addPointToPath(int row, int col)
    {
        if (this.data[row][col] != 0)
        {
            Pos pos = pointPool.obtain();// 记录点位置信息
            pos.row = row;
            pos.col = col;
            pathPoint.add(pos);
        }
    }

    private void calPath(int row, int col, int value)
    {
        int originRowValue = row;
        int originColValue = col;
        // 向左搜索'
        int left = originColValue - 1;
        if (left >= 0)
        {
            int setValue = originRowValue * SEED + left;// 计算行列构成的唯一Set值
                                                        // 用以判断记录表中是否已经有了记录
            if (!recordVistPointSet.contains(setValue))// 未在记录中
            {
                recordVistPointSet.add(setValue);// 加入记录中
                if (value == data[originRowValue][left])
                {// 路径联通
                    Pos pos = pointPool.obtain();// 记录点位置信息
                    pos.row = originRowValue;
                    pos.col = left;
                    pathPoint.add(pos);
                    calPath(originRowValue, left, value);// 递归搜索
                }
            }
        }
        // 向上搜索
        int top = originRowValue - 1;
        if (top >= 0)
        {// 在合法范围之内
            int setValue = top * SEED + originColValue;// 计算行列构成的唯一Set值
                                                       // 用以判断记录表中是否已经有了记录
            if (!recordVistPointSet.contains(setValue))// 未在记录中
            {
                recordVistPointSet.add(setValue);// 加入记录中
                if (value == data[top][originColValue])
                {// 路径联通
                    Pos pos = pointPool.obtain();// 记录点位置信息
                    pos.row = top;
                    pos.col = originColValue;
                    pathPoint.add(pos);
                    calPath(top, originColValue, value);// 递归搜索
                }
            }
        }
        // 向右侧搜索
        int right = originColValue + 1;
        if (right < colNum)
        {// 在合法范围内
            int setValue = originRowValue * SEED + right;// 计算行列构成的唯一Set值
                                                         // 用以判断记录表中是否已经有了记录
            if (!recordVistPointSet.contains(setValue))// 未在记录中
            {
                recordVistPointSet.add(setValue);// 加入记录中
                if (value == data[originRowValue][right])
                {// 路径联通
                    Pos pos = pointPool.obtain();// 记录点位置信息
                    pos.row = originRowValue;
                    pos.col = right;
                    pathPoint.add(pos);
                    calPath(originRowValue, right, value);// 递归搜索
                }
            }
        }
        // 向下搜索
        int bottom = originRowValue + 1;
        if (bottom < rowNum)
        {// 在合法范围之内
            int setValue = bottom * SEED + originColValue;// 计算行列构成的唯一Set值
                                                          // 用以判断记录表中是否已经有了记录
            if (!recordVistPointSet.contains(setValue))// 未在记录中
            {
                recordVistPointSet.add(setValue);// 加入记录中
                if (value == data[bottom][originColValue])
                {// 路径联通
                    Pos pos = pointPool.obtain();// 记录点位置信息
                    pos.row = bottom;
                    pos.col = originColValue;
                    pathPoint.add(pos);
                    calPath(pos.row, pos.col, value);// 递归搜索
                }
            }
        }
    }

    /**
     * 清理路径记录点
     */
    private void clearPathPoint()
    {
        pointPool.freeAll(pathPoint);// 清理路径记录点
        pathPoint.clear();
        recordVistPointSet.clear();// 清空记录访问数据结构
    }

    /**
     * 调整主矩阵
     */
    private void adjustMainMatrix()
    {
        for (int i = 0; i < colNum; i++)
        {
            adjusMatrixOneStep(data);
        }// end for i
    }

    /**
     * 计算出可 下落点
     * 
     * @param srcData
     * @param dst
     */
    private void setCanDropMatrix(int srcData[][], int[][] dst)
    {
        copyMatrix(srcData, dst);// 复制
        for (int j = 0; j < colNum; j++)
        {
            boolean isDrop = false;
            for (int i = 0; i < rowNum; i++)
            {
                if (srcData[i][j] == 0)
                {
                    isDrop = true;
                }
                if (isDrop && dst[i][j] != 0)
                {
                    // System.out.println("fuck");
                    dst[i][j] = CAN_DROP;
                }
            }// end for i
        }// end for j
    }

    /**
     * 调整一次矩阵
     * 
     * 
     * 1 1 1 1 1 1 0 0 0 0 0 0 1 0 1 0 1 0 0 1 0 1 0 1 1 1 1 1 1 1
     */
    private void adjusMatrixOneStep(int a[][])
    {
        for (int j = 0; j < colNum; j++)
        {
            for (int i = 1; i < rowNum; i++)
            {
                if (a[i][j] != 0)
                {
                    if (a[i - 1][j] == 0)
                    {
                        int temp = a[i - 1][j];
                        a[i - 1][j] = a[i][j];
                        a[i][j] = temp;
                    }// end if
                }
            }// end for i
        }// end for j
    }

    /**
     * 拷贝二维数组
     * 
     * @param src
     * @param dst
     */
    private void copyMatrix(int[][] src, int[][] dst)
    {
        for (int i = 0; i < rowNum; i++)
        {
            System.arraycopy(src[i], 0, dst[i], 0, colNum);
        }// end for i
    }

    /**
     * 比较两矩阵是否相等
     * 
     * @param matrix1
     * @param matrix2
     * @return
     * 
     *         返回0 表示相等 返回-1 表示不等
     */
    private boolean compareMatrix(int[][] a, int[][] b)
    {
        for (int i = 0; i < rowNum; i++)
        {
            for (int j = 0; j < colNum; j++)
            {
                if (a[i][j] != b[i][j])
                {
                    return false;
                }
            }// end for j
        }// end for i
        return true;
    }

    /**
     * 清空矩阵为0
     * 
     * @param a
     */
    private void clearMatrix(int[][] a)
    {
        for (int i = 0, rows = a.length; i < rows; i++)
        {
            for (int j = 0, col = a[i].length; j < a[i].length; j++)
            {
                a[i][j] = 0;
            }
        }// end for i
    }

    static class Pos
    {
        int row;
        int col;
    }

    public void dispose()
    {
        if (bombParticle != null)
        {
            bombParticle.dispose();
        }
    }
}// end class

