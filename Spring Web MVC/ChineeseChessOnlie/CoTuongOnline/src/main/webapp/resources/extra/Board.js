var campOrder =0;
var movers =0;
var isSent = false;
function ChessGame(boardId) {
    // Size and location parameters
    var isAI = true;
    var func = 1;

    var layout = {
        padding: 30,
        cell: 50,
        chessRadius: 20,
        fontSize: 36,
        width: 400,
        height: 450,
        offsetWidth: 460,
        offsetHeight: 510
    };
    // Color and font style
    var style = {
        board: {
            border: "#630",
            background: "#fed",
            font: "36px 隶书"
        },
        chess: [{
            border: "#fa8",
            background: "#fc9",
            font: "36px 隶书",
            fontColor: "#c00"
        }, {
            border: "#fa8",
            background: "#fc9",
            font: "36px 隶书",
            fontColor: "#090"
        }]
    };
    // Board assembly
    var Board = RootWidget.extend({
        auto: false,
        campOrder: 1, //上黑下红 Red on Heixia
        mover: 0,
        isMoving:false,
        boardMap:null,
        searchEngine:null,
        history:null,
        playerMove : [new Point(0,3),new Point(0,4),new Point(2,3),new Point(2,4),new Point(4,3),new Point(4,4),new Point(6,3),new Point(6,4)],
        playerMoveCount : 0,
        // Initialization
        //- Set the location, call the parent class event binding, the parent class calls the parent class implementation Drawing
        init: function () {

            this.offsetRect.left = 0;
            this.offsetRect.top = 0;
            this.offsetRect.right = 460;
            this.offsetRect.bottom = 510;
            this._super();
            this.history=new Array();
            this.boardMap=new Array(); // Init chess board
            for(var i=0;i<9;i++){ // 9 column
                this.boardMap[i]=new Array();
                for(var j=0;j<10;j++) // 10 row
                    this.boardMap[i][j]=null;
            }
            for (var i = 0; i < this.children.length; i++) {
                var child = this.children[i];
                if (child instanceof Chess && child.pos != null){
                    this.boardMap[child.pos.x][child.pos.y]=child;
                }
            }
            // this.searchEngine=new AlphaBetaEngine();
            this.searchEngine=new NegaScout_TT_HH();
            this.searchEngine.setMoveGenerator(new MoveGenerator());
            this.searchEngine.setEvaluator(new Evaluation());
            this.searchEngine.setSearchDepth(10);
        },

        changeMover: function (mover1){
            this.mover= mover1;
            console.log(" Mover change by Server : " +this.mover);
        },
        // Find the pieces on the board
        findChess: function (pos) {
            //console.log(this.id+" findChess"+ func++);
            if (!this.isValidPos(pos)) return null;
            for (var i = 0; i < this.children.length; i++) {
                var child = this.children[i];
                if (child instanceof Chess && child.pos != null && child.pos.equals(pos)) return child;
            }
            return null;
        },
        isGameOver: function(){
            // console.log(this.id+" isGameOver"+ func++);
            var red=false,
                black=false;
            for (var i = 0; i < this.children.length; i++) {
                var child = this.children[i];
                if (child instanceof Chess){
                    if(child.type=="R_KING") red=true;
                    else if(child.type=="B_KING") black=true;
                    if(red && black) return false;
                }
            }
            return true;
        },
        recordMove:function(chess, pos){
            var move=new Object();
            move.from=chess.pos;
            move.to=pos;
            move.target=this.findChess(pos);
            this.history.push(move);
            //console.log("PLAY_ID"+" "+chess.pos.x+","+chess.pos.y+" "+pos.x+","+pos.y+" "+chess.type+" "+chess.camp);
            if(!this.auto) {
                playerMove(chess.pos.x + "," + chess.pos.y + " " + pos.x + "," + pos.y + " " + chess.type + " " + movers);
                this.mover = 1- this.mover;
                console.log("Mover Change auto : "+this.mover);
            }
            else {
                this.auto = false;
            }
        },
        restore:function(){
            // console.log(this.id+" restore"+ func++);
            if(this.history.length%2==1 ||this.history.length==0)return;
            for(var i=0;i<2;++i){
                var move=this.history.pop();
                chess=this.findChess(move.to);
                // Move the pieces on the board reverse current
                chess.autoMoveBackTo(move.from);
                // If there had been eaten pieces
                if(move.target!=null){
                    // Add pieces have been eaten
                    this.addChild(move.target);
                    this.boardMap[move.to.x][move.to.y]=move.target;
                    // Move animations, from itself to a movement
                    move.target.autoMoveBackTo(move.to);
                }
            }
            this.mover=0;
        },
        // Do not keep a record of when you move a pawn for undo
        moveChessWithoutRecored:function(chess,pos){
            console.log(this.id+" moveChessWithoutRecord"+ func++);
            this.boardMap[pos.x][pos.y]=chess;// modify the map
            if(!chess.pos.equals(pos)){// place to move, do not clear out
                this.boardMap[chess.pos.x][chess.pos.y]=null; // remove the pieces of the original location
                chess.pos = pos;
            }
        },
        // Move the chess pieces, chess party line changed
        moveChess: function (chess, pos) {

            // console.log(this.id+" moveChess"+func++);

            this.recordMove(chess,pos);
            this.removeChess(pos);
            this.boardMap[pos.x][pos.y]=chess;
            this.boardMap[chess.pos.x][chess.pos.y]=null;
            chess.pos = pos;
            // change the mover turn mover = 0 - player, mover =1 - computer
            // this.mover=1-chess.camp;
            //  this.computerMoveChess();
            // If the game ends
            if(this.isGameOver()){
                if(this.mover==1) alert("恭喜，你赢啦！");
                else alert("输了哦，下次再努力吧～");
                this.mover=-1;
                return;
            }
        },

        MoveEnemyChess: function (from, to,camp) {
            var Chess = this.findChess(from);
            this.auto = true;
            Chess.autoMoveTo(to);

        },


        computerMoveChess:function(){
            // If you turn the computer side line chess
            //  console.log(this.id+" computerMoveChess"+ func++);
            if(this.mover==1){

                var _this=this;
                var timer=setInterval(function(){
                    if(_this.isMoving) return;
                    clearInterval(timer);
                    var bestMove=_this.searchEngine.searchAGoodMove(_this.boardMap);
                    if(bestMove.score<=-19990) {
                        alert("我输了~");
                        return;
                    }

                    if(_this.playerMoveCount<4) {
                        var Chess=_this.findChess(_this.playerMove[_this.playerMoveCount]);
                        Chess.autoMoveTo(_this.playerMove[_this.playerMoveCount+1]);
                        _this.playerMoveCount+=2;
                    }
                    else {

                        var Chess = _this.findChess(bestMove.from);
                        Chess.autoMoveTo(bestMove.to);
                    }

                },100);
            }
        },
        // Remove the pieces
        removeChess: function (pos) {

            console.log(this.id+" removeChess"+ func++);
            this.boardMap[pos.x][pos.y]=null;
            var chess = this.findChess(pos);
            if (chess != null) this.removeChild(chess);
        },
        // Invalid location, existing pieces or cross-border
        isValidPos: function (pos) {
            // console.log(this.id+" isValidPos"+ func++);
            return pos != null && !this.isOutsideBoard(pos);
        },
        // In the river boundary
        isInsideCamp: function (pos, camp) {
            if (!this.isValidPos(pos)) return false;
            if (camp == this.campOrder) {
                return pos.y <= 4;
            } else {
                return pos.y >= 5;
            }
        },
        // In nine intrauterine
        isInsidePalace: function (pos, camp) {
            if (!this.isValidPos(pos)) return false;
            if (pos.x < 3 || pos.x > 5) return false;
            if (camp == this.campOrder) {
                return pos.y <= 2;
            } else {
                return pos.y >= 7;
            }
        },
        // Out of bounds
        isOutsideBoard:function(pos){
            // console.log(this.id+" isOutsideBoard"+ func++);
            return pos.x<0 || pos.x>=9 || pos.y<0 || pos.y>=10;
        },
        // Drawing board
        onPaint: function () {
            //  console.log(this.id+" onPaint"+ func++);
            str="<table cellspacing=0 border=1>";
            for(var j=0;j<10;j++){
                str+="<tr>";
                for(var i=0;i<9;i++){
                    str+="<td style='height:15px;width:40px;'>";
                    if(this.boardMap[i][j]!=null)
                        str+=this.boardMap[i][j].id;
                    str+="</td>";
                }
                str+="</tr>";
            }
            str+="</table>";
            document.getElementById("debug").innerHTML=str;

            var ctx = this.canvas.getContext('2d');
            // Rectangular checkerboard background
            ctx.fillStyle = style.board.background;
            ctx.beginPath();
            ctx.rect(0, 0, layout.offsetWidth, layout.offsetHeight);
            ctx.fill();
            ctx.closePath();
            // Line
            var p = layout.padding,
                s = layout.cell,
                w = layout.width,
                h = layout.height;
            ctx.strokeStyle = style.board.border;
            ctx.lineWidth = 2;
            ctx.beginPath();
            // 10 horizontal
            for (var i = 0; i < 10; i++) {
                ctx.moveTo(p, s * i + p);
                ctx.lineTo(w + p, s * i + p);
            }
            // Left sideline
            ctx.moveTo(p, p);
            ctx.lineTo(p, h + p);
            ctx.moveTo(w + p, p);
            ctx.lineTo(w + p, h + p);
            // 7 disconnected vertical
            for (var i = 1; i < 8; i++) {
                ctx.moveTo(s * i + p, p);
                ctx.lineTo(s * i + p, s * 4 + p);
                ctx.moveTo(s * i + p, s * 5 + p);
                ctx.lineTo(s * i + p, h + p);
            }
            // Slash
            ctx.moveTo(s * 3 + p, p);
            ctx.lineTo(s * 5 + p, s * 2 + p);
            ctx.moveTo(s * 5 + p, 0 + p);
            ctx.lineTo(s * 3 + p, s * 2 + p);
            ctx.moveTo(s * 3 + p, s * 7 + p);
            ctx.lineTo(s * 5 + p, s * 9 + p);
            ctx.moveTo(s * 5 + p, s * 7 + p);
            ctx.lineTo(s * 3 + p, s * 9 + p);
            ctx.stroke();
            ctx.closePath();
            // Text
            ctx.save();
            ctx.rotate(-Math.PI/2);
            ctx.font = style.board.font;
            ctx.fillStyle = style.board.border;
            ctx.textAlign = "center";
            ctx.textBaseline = "middle";
            ctx.fillText("楚", -(p+s*4.5), (p + s * 1.5));
            ctx.fillText("河", -(p+s*4.5), (p + s * 2.5));
            ctx.rotate(Math.PI);
            ctx.fillText("漢", (p+s*4.5), -(p+s*6.5));
            ctx.fillText("界", (p+s*4.5), -(p+s*5.5));
            ctx.restore();
        }
    });


    // Piece assembly
    var Chess = Widget.extend({
        name: null,
        type: null,
        camp: null,// 0-- red, black 1--
        pos: null,
        isDragging: false,
        targetPos: null,
        targetIndicatorAlpha: 0.2,
        create: function (id, parent, name, type, camp, pos) {
            this._super(id, parent);
            this.name = name;
            this.type = type;
            this.camp = camp || 0;
            this.pos = pos || new Point(0, 0);
            this.offsetRect.left = layout.padding + layout.cell * this.pos.x - layout.cell / 2;
            this.offsetRect.top = layout.padding + layout.cell * this.pos.y - layout.cell / 2;
            this.offsetRect.right = this.offsetRect.left + layout.cell;
            this.offsetRect.bottom = this.offsetRect.top + layout.cell;
        },
        // Draw pieces
        onPaint: function () {
            var ctx = this.canvas.getContext('2d');
            ctx.fillStyle = style.chess[this.camp].background;
            ctx.strokeStyle = style.chess[this.camp].border;
            ctx.font = style.chess[this.camp].font;
            // Center
            var x = this.offsetRect.left + layout.cell / 2,
                y = this.offsetRect.top + layout.cell / 2;
            ctx.beginPath();
            ctx.fillStyle = "rgba(0, 0, 0, 0.2)";
            // Shadow
            if (this.isDragging) ctx.arc(x + 2, y + 4, layout.chessRadius + 1, 0, 360);
            else ctx.arc(x + 1, y + 2, layout.chessRadius + 1, 0, 360);
            ctx.fill();
            ctx.fillStyle = style.chess[this.camp].background;
            ctx.closePath();
            // Position translucent Tips
            if (this.targetPos != null && this.targetIndicatorAlpha > 0) {
                ctx.beginPath();
                ctx.fillStyle = "rgba(0, 128, 0, " + this.targetIndicatorAlpha + ")";
                ctx.arc(layout.padding + this.targetPos.x * layout.cell, layout.padding + this.targetPos.y * layout.cell, layout.cell / 2, 0, 360);
                ctx.fill();
                ctx.fillStyle = style.chess[this.camp].background;
                ctx.closePath();
            }
            // Piece body
            ctx.beginPath();
            ctx.arc(x, y, layout.chessRadius, 0, 360);
            ctx.fill();
            ctx.textAlign = "center";
            ctx.textBaseline = "middle";
            ctx.fillStyle = "rgba(255, 255, 255, 0.5)";
            ctx.fillText(this.name, x + 1, y - layout.fontSize / 16 + 1);
            ctx.fillStyle = style.chess[this.camp].fontColor;
            ctx.fillText(this.name, x, y - layout.fontSize / 16);
            ctx.stroke();
            ctx.closePath();
        },
        // Mouse click
        onMouseDown: function (point) {
            if(this.parent.isMoving)return;
            if (this.parent.mover == this.camp && this.parent.mover == campOrder) {// confirm that party line chess
                this.isDragging = true;// drag the pieces
                this.parent.moveChildToTop(this);// adjust to the uppermost layer
                this.parent.redraw();// redraw
            }
        },
        // When the mouse moves
        onMouseMove: function (point) {
            if (this.isDragging) {// If you are dragging pieces
                if( point.x <= 0 || point.x >= layout.offsetWidth || point.y <= 0 || point.y >= layout.offsetHeight){
                    this.isDragging = false;  // Stop dragging
                    this.moveTo(this.pos); // Undo Move
                    this.parent.redraw();
                    return;
                }
                //According to the center coordinates of the upper left corner // Inverse rectangular area
                var x = point.x - layout.cell / 2,
                    y = point.y - layout.cell / 2;
                // Move pieces
                this.offsetRect.moveTo(x, y);
                //Coordinates on the board according to the screen coordinates // Calculate
                var pos = this.point2chessPos(x, y);
                // Verify the target position
                if (this.isTargetValid(pos)) this.targetPos = pos;
                else this.targetPos = null;
                // Redraw
                this.parent.redraw();
            }
        },
        // Mouse up
        onMouseUp: function (point) {
            if (this.isDragging) { // drag the pieces in
                this.isDragging = false;// Stop dragging
                var pos = this.targetPos || this.pos;
                this.moveTo(pos);// move pieces
                if (this.targetPos != null) {// notify the board to change the position of the pieces
                    this.parent.moveChess(this, pos);
                }
            }
        },
        autoMoveBackTo:function(pos){
            this.targetPos=pos; // destination
            this.moveTo(pos);// move the pawn animation
            if (this.targetPos != null) {// notify the board to change the position of the pieces
                this.parent.moveChessWithoutRecored(this, pos);
            }
        },
        autoMoveTo:function(pos){
            console.log(this.id+" autoMoveTo"+ func++);
            this.targetPos=pos;
            this.moveTo(pos);// move pieces
            if (this.targetPos != null) {// notify the board to change the position of the pieces
                this.parent.moveChess(this, pos);
            }
        },
        // Turn checkerboard canvas coordinate coordinates
        point2chessPos: function (x, y) {
            return new Point(Math.ceil((x - layout.padding) / layout.cell), Math.ceil((y - layout.padding) / layout.cell));
        },
        chessPos2point: function (x, y) {},
        // Move the pieces of animation
        moveTo: function (pos) {
            this.parent.isMoving=true;
            // Target position, the upper left corner
            var left = layout.padding + layout.cell * pos.x - layout.cell / 2,
                top = layout.padding + layout.cell * pos.y - layout.cell / 2;
            // Target position offset of the current location
            var dx = left - this.offsetRect.left,
                dy = top - this.offsetRect.top;
            // Animation
            // - 40ms frequency
            var t = 0,
                c = 15,
                _this = this;
            var timer = setInterval(function () {
                // End
                if (++t > c) {
                    clearInterval(timer);
                    _this.pos = pos;
                    _this.offsetRect.moveTo(left, top);
                    _this.targetPos = null;
                    _this.targetIndicatorAlpha = 0.2;
                    _this.parent.isMoving=false;
                    return;
                }
                var ratio = 0;
                if (t <= c / 2) {// the first half
                    ratio = 2 * t / c;// with time, the rate of increase
                    ratio = 1 - 0.5 * ratio * ratio * ratio * ratio;// time shift is 4 times the power
                } else {
                    ratio = 2 - 2 * t / c;
                    ratio = 0.5 * ratio * ratio * ratio * ratio;
                }
                _this.offsetRect.moveTo(left - dx * ratio, top - dy * ratio);
                _this.targetIndicatorAlpha = 0.2 * ratio;
                _this.parent.redraw();
            }, 40);
        },
        isTargetValid: function (pos) {
            if (!this.parent.isValidPos(pos)) return false;
            var chess = this.parent.findChess(pos);
            return chess == null || chess.camp != this.camp;
        },
        isRed:function(){
            return this.camp==0;
        },
        isSameCamp:function(chess){
            return this.camp==chess.camp;
        }
    });
    // Car
    var Chariot = Chess.extend({
        create: function (id, parent, camp, pos) {
            this._super(id, parent, "車", camp==0?"R_CAR":"B_CAR", camp, pos);
        },
        isTargetValid: function (pos) {
            if (!this._super(pos)) return false;
            var dx = pos.x - this.pos.x,
                dy = pos.y - this.pos.y;
            if (dx != 0 && dy != 0) return false;
            var targetChess = this.parent.findChess(pos);
            var steps = Math.max(Math.abs(dx), Math.abs(dy));
            var blockPos = new Point(this.pos.x, this.pos.y);
            for (var i = 1; i < steps; i++) {
                blockPos.x += dx / steps;
                blockPos.y += dy / steps;
                if (this.parent.findChess(blockPos) != null) return false;
            }
            return true;
        }
    });
    // Horse
    var Horse = Chess.extend({
        create: function (id, parent, camp, pos) {
            this._super(id, parent, "馬", camp==0?"R_HORSE":"B_HORSE", camp, pos);
        },
        isTargetValid: function (pos) {
            if (!this._super(pos)) return false;
            var dx = pos.x - this.pos.x,
                dy = pos.y - this.pos.y;
            if (dx == 0 || dy == 0 || Math.abs(dx) + Math.abs(dy) != 3) return false;
            var targetChess = this.parent.findChess(pos);
            var blockPos = new Point(this.pos.x, this.pos.y);
            if (Math.abs(dx) == 2) blockPos.x += dx / 2;
            else blockPos.y += dy / 2;
            return this.parent.findChess(blockPos) == null;
        }
    });
    // Like / phase
    var Elephant = Chess.extend({
        create: function (id, parent, camp, pos) {
            this._super(id, parent, camp == 0 ? "相" : "象", camp==0?"R_ELEPHANT":"B_ELEPHANT", camp, pos);
        },
        isTargetValid: function (pos) {
            if (!this._super(pos)) return false;
            if (!this.parent.isInsideCamp(pos, this.camp)) return false;
            var dx = pos.x - this.pos.x,
                dy = pos.y - this.pos.y;
            if (Math.abs(dx) != 2 || Math.abs(dy) != 2) return false;
            var blockPos = new Point(this.pos.x + dx / 2, this.pos.y + dy / 2);
            return this.parent.findChess(blockPos) == null;
        }
    });
    // Disabilities
    var Guard = Chess.extend({
        create: function (id, parent, camp, pos) {
            this._super(id, parent, camp == 0 ? "士" : "仕", camp==0?"R_BISHOP":"B_BISHOP", camp, pos);
        },
        isTargetValid: function (pos) {
            if (!this._super(pos)) return false;
            if (!this.parent.isInsidePalace(pos, this.camp)) return false;
            var dx = pos.x - this.pos.x,
                dy = pos.y - this.pos.y;
            if (Math.abs(dx) != 1 || Math.abs(dy) != 1) return false;
            return true;
        }
    });
    // Set / handsome
    var General = Chess.extend({
        create: function (id, parent, camp, pos) {
            this._super(id, parent, camp == 0 ? "帥" : "將", camp==0?"R_KING":"B_KING", camp, pos);
        },
        isTargetValid: function (pos) {
            if (!this._super(pos)) return false;
            var x,y;
            var target=this.parent.boardMap[pos.x][pos.y];
            if(target!=null){
                //alert(target.type);
                if (this.type=="R_KING" && target.type=="B_KING"){
                    if(this.pos.x!==pos.x) return false;
                    for(x=this.pos.x,y=this.pos.y-1;y>0 && this.parent.boardMap[x][y]==null;--y);
                    if(y>=0 && this.parent.boardMap[x][y].type=="B_KING") return true;
                } else if (this.type=="B_KING" && target.type=="R_KING") {
                    if(this.pos.x!=pos.x) return false;
                    for(x=this.pos.x,y=this.pos.y+1;y<10 && this.parent.boardMap[x][y]==null;++y);
                    if(y<10 && this.parent.boardMap[x][y].type=="R_KING") return true;
                }
            }
            if (!this.parent.isInsidePalace(pos, this.camp)) return false;
            var dx = pos.x - this.pos.x,
                dy = pos.y - this.pos.y;
            if (Math.abs(dx) + Math.abs(dy) != 1) return false;
            return true;
        }
    });
    // Gun
    var Cannon = Chess.extend({
        create: function (id, parent, camp, pos) {
            this._super(id, parent, camp == 0 ? "炮" : "砲", camp==0?"R_CANON":"B_CANON", camp, pos);
        },
        isTargetValid: function (pos) {
            if (!this._super(pos)) return false;
            var dx = pos.x - this.pos.x,
                dy = pos.y - this.pos.y;
            if (dx != 0 && dy != 0) return false;
            var targetChess = this.parent.findChess(pos);
            var steps = Math.max(Math.abs(dx), Math.abs(dy));
            var blockPos = new Point(this.pos.x, this.pos.y);
            var blocks = 0;
            for (var i = 1; i < steps; i++) {
                blockPos.x += dx / steps;
                blockPos.y += dy / steps;
                if (this.parent.findChess(blockPos) != null) blocks++;
            }
            return (blocks == 0 && targetChess == null) || (blocks == 1 && targetChess != null);
        }
    });
    // Bing / death
    var Pawn = Chess.extend({
        create: function (id, parent, camp, pos) {
            this._super(id, parent, camp == 0 ? "兵" : "卒", camp==0?"R_PAWN":"B_PAWN", camp, pos);
        },
        isTargetValid: function (pos) {
            if (!this._super(pos)) return false;
            var dx = pos.x - this.pos.x,
                dy = pos.y - this.pos.y;
            if(pos.x == 6 && pos.y == 5)
                console.log("wait");
            if (this.parent.isInsideCamp(pos, this.camp) && dx != 0) return false;
            if (this.camp == this.parent.campOrder && dy < 0) return false;
            else if (this.camp != this.parent.campOrder && dy > 0) return false;
            if (Math.abs(dx) + Math.abs(dy) != 1) return false;
            return true;
        }
    });
    // Moves generator
    var MoveGenerator=Class.extend({
        moveList:null, // moves the list: staggered array, moveList [depth] [i] denotes the i-th layer depth moves, depth range 0..maxDepth
        moveCount:0,// number of law go, staggered array of the second dimension of length
        create:function(){
            this.moveList=new Array();
            for(var i=0;i<15;++i)
                this.moveList[i]=new Array();
            this.moveCount=0;
        },
        createPossibleMove:function(map, depth, isRedTurn){
            this.moveCount = 0;
            this.moveList[depth].length=0;
            var chess;
            // Traverse the chessboard, the pieces one by one generation moves
            for (var j = 0; j < 10; ++j)
                for (var i = 0; i < 9; ++i)
                    if ((chess=map[i][j]) != null)
                    {
                        if ((!isRedTurn && chess.isRed()) ||(isRedTurn && !chess.isRed())) continue;
                        var pos=new Point(i,j);
                        if(chess.type=="R_KING" || chess.type=="B_KING") this.genKingMove(map, pos, depth);
                        else if(chess.type=="R_BOSHIP" || chess.type=="B_BOSHIP") this.genBishopMove(map, pos, depth);
                        else if(chess.type=="R_ELEPHANT" || chess.type=="B_ELEPHANT") this.genElephantMove(map, pos, depth);
                        else if(chess.type=="R_HORSE" || chess.type=="B_HORSE") this.genHorseMove(map, pos, depth);
                        else if(chess.type=="R_CAR" || chess.type=="B_CAR") this.genCarMove(map, pos, depth);
                        else if(chess.type=="R_PAWN" || chess.type=="B_PAWN") this.genPawnMove(map, pos, depth);
                        else if(chess.type=="R_CANON" || chess.type=="B_CANON") this.genCanonMove(map, pos, depth);
                    }
            return this.moveCount;
        },
        genKingMove:function(map, pos, depth){
            // Left right
            var dx=[-1,0,1,0],
                dy=[0,-1,0,1];
            var chess=map[pos.x][pos.y];
            // Nine intrauterine moves
            for (var k=0;k<4;++k) {
                var x=pos.x+dx[k],y=pos.y+dy[k];// move
                if (y<0 || y>=10 || x<0 || x>=9) continue;// out of bounds
                if ((x<3 || x>5) || (chess.type=="R_KING" && y<7) || (chess.type=="B_KING" && y>2)) continue;// out JiuGongTu
                if(map[x][y]!=null && chess.camp==map[x][y].camp) continue;// has 方有子
                this.addMove(pos, new Point(x,y), depth);
            }
            // Kill red on black
            if (chess.type=="R_KING") {
                for(x=pos.x,y=pos.y-1;y>=0 && map[x][y]==null;--y);// find up along the first piece, or until out of bounds
                if(y>=0 && map[x][y].type=="B_KING") this.addMove(pos,new Point(x,y), depth);// not out of bounds and the first piece of black Shuai
            }
            // Kill black on red
            else if (chess.type=="B_KING") {
                for(x=pos.x,y=pos.y+1;y<10 && map[x][y]==null;++y);// find along down the first piece, or until out of bounds
                if(y<10 && map[x][y].type=="R_KING") this.addMove(pos,new Point(x,y), depth);// not out of bounds and the first piece of red will
            }
        },
        genBishopMove:function(map, pos, depth){
            // Upper left, upper right, lower right, lower left
            var dx=[-1,1,1,-1],
                dy=[-1,-1,1,1];
            var chess=map[pos.x][pos.y];
            // Nine intrauterine moves
            for (var k=0;k<4;++k){
                var x=pos.x+dx[k],y=pos.y+dy[k];// move
                if (y<0 || y>=10 || x<0 || x>=9) continue;// out of bounds
                if ((x<3 || x>5) || (chess.type=="R_BISHOP" && y<7) || (chess.type=="B_BISHOP" && y>2))continue;// out JiuGongTu
                if(map[x][y]!=null && chess.camp==map[x][y].camp)continue; // has 方有子
                this.addMove(pos, new Point(x,y), depth);
            }
        },
        genElephantMove:function(map, pos, depth){
            // Upper left, upper right, lower right, lower left
            var dx=[-2,2,2,-2],
                dy=[-2,-2,2,2];
            var chess=map[pos.x][pos.y];
            for (var k=0;k<4;++k) {
                var x=pos.x+dx[k],y=pos.y+dy[k];
                if (y<0 || y>=10 || x<0 || x>=9) continue; // out of bounds
                if ((chess.type=="R_ELEPHANT" && y<5) || (chess.type=="B_ELEPHANT" && y>4)) continue;// river
                if(map[Math.floor((pos.x+x)/2)][Math.floor((pos.y+y)/2)] != null) continue;// stuffed elephant eye
                if(map[x][y]!=null && chess.camp==map[x][y].camp) continue;//己方有子
                this.addMove(pos, new Point(x,y), depth);
            }
        },
        genHorseMove:function(map, pos, depth){
            // Clockwise
            var dx=[1,2,2,1,-1,-2,-2,-1],
                dy=[-2,-1,1,2,2,1,-1,-2];
            var chess=map[pos.x][pos.y];
            for (var k=0;k<8;++k) {
                var x=pos.x+dx[k],y=pos.y+dy[k];
                if (y<0 || y>=10 || x<0 || x>=9) continue;// out of bounds
                if(map[pos.x+parseInt(dx[k]/2)][pos.y+parseInt(dy[k]/2)] != null) continue;// trip Matui
                if(map[x][y]!=null && chess.camp==map[x][y].camp) continue;//己方有子
                this.addMove(pos, new Point(x,y), depth);
            }
        },
        genCarMove:function(map, pos, depth){
            var x,y;
            var chess = map[pos.x][pos.y];
            // Right, the current location of the pieces on the right to empty the border or the first among all the pieces to go.
            for(x=pos.x+1,y=pos.y;x<9 && null == map[x][y];++x)this.addMove(pos, new Point(x, y), depth);
            // If not out of bounds, and the first piece is not one's own, you can kill the child.
            if(x<9 && chess.camp!=map[x][y].camp) this.addMove(pos, new Point(x, y), depth);
            // Left
            for(x=pos.x-1,y=pos.y;x >= 0 && null == map[x][y];--x)this.addMove(pos, new Point(x, y), depth);
            if(x>=0 && chess.camp!=map[x][y].camp) this.addMove(pos, new Point(x, y), depth);
            // Next
            for(x=pos.x,y=pos.y+1;y < 10 && null == map[x][y];++y) this.addMove(pos, new Point(x, y), depth);
            if(y<10 && chess.camp!=map[x][y].camp) this.addMove(pos, new Point(x, y), depth);
            // On
            for(x =pos.x,y =pos.y-1;y>=0 && null == map[x][y];--y) this.addMove(pos, new Point(x, y), depth);
            if(y>=0 && chess.camp!=map[x][y].camp) this.addMove(pos, new Point(x, y), depth);
        },
        genPawnMove:function(map, pos, depth){
            var x,y;
            var chess = map[pos.x][pos.y];
            if((chess.type=="R_PAWN" && pos.y < 5)||(chess.type=="B_PAWN" && pos.y>4)) {// has to cross the river
                y=pos.y;
                x=pos.x+1;// Right
                if(x < 9 && (map[x][y]==null || chess.camp!=map[x][y].camp)) this.addMove( pos, new Point(x, y), depth);
                x=pos.x-1; // Left
                if(x >= 0 && (map[x][y]==null || chess.camp!=map[x][y].camp)) this.addMove( pos, new Point(x, y), depth);
            }
            x=pos.x;
            y=pos.y+(chess.isRed()?-1:1);
            if ((chess.type=="R_PAWN" && y<0) || (chess.type=="B_PAWN" && y>=10)) return;// out of bounds
            if(map[x][y]!=null && chess.camp==map[x][y].camp) return;//己方有子
            this.addMove(pos, new Point(x, y), depth);
        },
        genCanonMove:function(map, pos, depth){
            var x, y;
            var chess = map[pos.x][pos.y];
            // Right, the current location of the pieces on the right to empty the border or the first among all the pieces to go.
            for(x=pos.x+1,y=pos.y; x<9 && null == map[x][y]; ++x) this.addMove(pos, new Point(x, y), depth);
            // If not out of bounds, then crossed the first piece, then find the first piece
            for(++x; x<9 && null == map[x][y]; ++x);
            // If not out of bounds, and the first piece is not one's own, then kill the child
            if(x<9 && chess.camp!=map[x][y].camp) this.addMove(pos, new Point(x, y), depth);
            // Left
            for(x=pos.x-1,y=pos.y; x>=0 && null == map[x][y]; --x) this.addMove(pos, new Point(x, y), depth);
            for(--x; x>=0 && null == map[x][y]; --x);
            if(x>=0 && chess.camp!=map[x][y].camp) this.addMove(pos, new Point(x, y), depth);
            // Next
            for(x=pos.x,y=pos.y+1; y<10 && null == map[x][y]; ++y) this.addMove(pos, new Point(x, y), depth);
            for(++y; y<10 && null == map[x][y]; ++y);
            if(y<10 && chess.camp!=map[x][y].camp) this.addMove(pos, new Point(x, y), depth);
            // On
            for(x=pos.x,y=pos.y-1; y>=0 && null == map[x][y]; --y) this.addMove(pos, new Point(x, y), depth);
            for(--y; y>=0 && null == map[x][y]; --y);
            if(y>=0 && chess.camp!=map[x][y].camp) this.addMove(pos, new Point(x, y), depth);
        },
        addMove:function(from, to, depth){
            this.moveList[depth][this.moveCount] = new Object();
            this.moveList[depth][this.moveCount].from = from;
            this.moveList[depth][this.moveCount].to = to;
            this.moveList[depth][this.moveCount].score=0;
            this.moveCount++;
            return this.moveCount;
        }
    });
    // Search engine base class
    var SearchEngine=Class.extend({
        currentMap:null,
        searchDepth:3,
        evaluator:null,
        moveGen:null,
        bestMove:null,
        create:function(){
            this.currentMap=new Array();
            for(var i=0;i<9;++i){
                this.currentMap[i]=new Array();
                for(var j=0;j<10;++j)
                    this.currentMap[i][j]=null;
            }
        },
        searchAGoodMove:function(){},
        setSearchDepth:function(depth){
            this.searchDepth = depth;
        },
        setEvaluator:function(evaluator){
            this.evaluator = evaluator;
        },
        setMoveGenerator:function(MG){
            this.moveGen = MG;
        },
        makeMove:function(move){
            var chess = this.currentMap[move.to.x][move.to.y];
            this.currentMap[move.to.x][move.to.y] = this.currentMap[move.from.x][move.from.y];
            this.currentMap[move.from.x][move.from.y] = null;
            return chess;
        },
        unMakeMove:function(move, chess){
            this.currentMap[move.from.x][move.from.y] = this.currentMap[move.to.x][move.to.y];
            this.currentMap[move.to.x][move.to.y] = chess;
        },
        mapCopy:function(map){
            for(var i=0;i<9;++i)
                for(var j=0;j<10;++j){
                    var chess=map[i][j];
                    if(chess!=null)
                        this.currentMap[i][j]=new Chess(chess.id, null, chess.name, chess.type, chess.camp, new Point(chess.pos.x,chess.pos.y));
                    else
                        this.currentMap[i][j]=null;
                }
        },
        isGameOver:function(map, depth){
            var red = false, // Red Fang Zhuang survival situation
                black = false; // Black survival situation
            // Traverse above the base camp
            for (var j = 7; j < 10; ++j)
                for (var i = 3; i < 6; ++i)
                    if (map[i][j]!=null){
                        if (map[i][j].type == "B_KING") black = true;
                        if (map[i][j].type == "R_KING") red  = true;
                    }
            // Traverse the base camp below
            for (var j = 0; j < 3; ++j)
                for (var i = 3; i < 6; i++)
                    if(map[i][j]!=null){
                        if (map[i][j].type == "B_KING") black = true;
                        if (map[i][j].type == "R_KING") red  = true;
                    }

            // Since the top down, depth decreasing, leaves zero. That is, the number of layers = maxDepth-depth.
            // - An even layer of one's own, odd layers for each other playing chess.
            // - This program, for one's own Black
            var isBlackTurn = (this.maxDepth - depth + 1) % 2;
            // Own round victory, positive infinity, otherwise, negative infinity.
            return (red && black)?0:(black^isBlackTurn?-1:1)*(19990 + depth);
        }
    });
    // History Table
    var HistoryHeuristic=Class.extend({
        historyTable:null,
        targetBuff:null,
        create:function(){
            this.historyTable=new Array();
            for(var f=0;f<90;++f){
                this.historyTable[f]=new Array();
                for(var t=0;t<90;++t)
                    this.historyTable[f][t]=0;
            }
            this.targetBuff=new Array();
        },
        resetHistoryTable:function(){
            for(var f=0;f<90;++f)
                for(var t=0;t<90;++t)
                    this.historyTable[f][t]=0;
        },
        getHistoryScore:function(move){
            var from = move.from.y*9+move.from.x,
                to = move.to.y*9+move.to.x;
            return this.historyTable[from][to];
        },
        enterHistoryScore:function(move,depth){
            var from = move.from.y*9+move.from.x,
                to = move.to.y*9+move.to.x;
            this.historyTable[from][to] += 2<<depth;
        },
        mergeSort:function(source, n, direction){
            var s = 1;
            while(s < n)
            {
                this.mergeAll(source, this.targetBuff, s, n, direction);
                s += s;
                this.mergeAll(this.targetBuff, source, s, n, direction);
                s += s;
            }
        },
        // Merge two adjacent size piece array s
        merge:function(source, target, l, m, r,direction){
            var i = l,
                j = m + 1,
                k = l;
            while((i <= m) && (j <= r))
                if (direction^(source[i].score >= source[j].score))
                    target[k++] = source[i++];
                else
                    target[k++] = source[j++];
            if(i > m)
                for (var q = j; q <= r; ++q)target[k++] = source[q];
            else
                for(var q = i; q <= m; ++q)target[k++] = source[q];
        },
        mergeAll:function(source, target, s, n, direction){
            var i = 0;
            while(i <= n - 2 * s){
                // Merge two adjacent size piece array s
                this.merge(source, target, i, i + s - 1, i + 2 * s - 1,direction);
                i=i+2*s;
            }
            if (i + s < n) // remaining number of elements less than 2s
                this.merge(source, target, i, i + s - 1, n - 1,direction);
            else
                for (var j = i; j <= n - 1; ++j) target[j] = source[j];
        }
    });
    // Permutation table class
    // - Using Zobrist hashing
    // - hashCheck commonly used 64bit numbers. Here because js does not support 64bit
    var TranspositionTable=Class.extend({
        hashKey32:null,
        hashKey32Table:null,
        hashCheck:null,
        TT:null,
        // Returns a random integer 32bit
        rand32:function(){
            return (Math.floor(Math.random()*(~(1<<31))))^((~(1<<31))&((Math.floor(Math.random()*(~(1<<31))))<<15))^((~(1<<31))&((Math.floor(Math.random()*(~(1<<31))))<<30));
        },
        // Initialize the hash value of the board
        initializeHashKey:function(){
            // Assignment: hashTable [pawn species] [x abscissa] [y coordinate], 32bit random integer
            this.hashKey32Table=new Array();
            for (var i = 0; i < 15; ++i){
                this.hashKey32Table[i]=new Array();
                for (var j = 0; j < 9; ++j){
                    this.hashKey32Table[i][j]=new Array();
                    for (var k = 0; k < 10; ++k){
                        this.hashKey32Table[i][j][k] = this.rand32();
                    }
                }
            }
            // Permutation table. TT [0] Black, TT [1] the red side.
            this.TT=new Array();
            this.TT[0] = new Array(1<<20);
            this.TT[1] = new Array(1<<20);
            for(var i=0;i<2;++i)
                for(var j=0;j<(1<<20);++j){
                    this.TT[i][j]=new Object();
                    this.TT[i][j].checkSum = 0;
                    this.TT[i][j].entryType = "";
                    this.TT[i][j].eval = 0;
                    this.TT[i][j].depth = 0;
                }
        },
        // Constructor
        create:function(){
            this.initializeHashKey();
        },
        // Piece is mapped to an integer, so the subscript
        // - The program can be used in all kinds of pieces integer, this step can be avoided, but it will reduce the readability.
        convertChessToNumber:function(chess){
            if(chess==null) return 0;
            var chessType=["","B_KING","B_CAR","B_HORSE","B_CANON","B_BISHOP","B_ELEPHANT","B_PAWN",
                "R_KING","R_CAR","R_HORSE","R_CANON","R_BISHOP","R_ELEPHANT","R_PAWN"];
            for(var i=0;i<chessType.length;++i)
                if(chess.type==chessType[i])
                    return i;
        },
        // Calculate the hash value of the current chess game
        calculateInitHashKey:function(map){
            this.hashKey32 = 0;
            this.hashCheck = "";// each position by a two-digit number,
            for (j = 0; j < 9; ++j)
                for (k = 0; k < 10; ++k){
                    var chessType=this.convertChessToNumber(map[j][k]);
                    if (chessType != 0)
                        this.hashKey32 = this.hashKey32 ^ this.hashKey32Table[chessType][j][k];
                    this.hashCheck += String.fromCharCode(chessType+56);
                }
        },
        // Increment hash value is calculated chess move map after the operation.
        hashMakeMove:function(move,map){
            var fromId = this.convertChessToNumber(map[move.from.x][move.from.y]),
                toId= this.convertChessToNumber(map[move.to.x][move.to.y]);
            this.hashKey32 = this.hashKey32 ^ this.hashKey32Table[fromId][move.from.x][move.from.y];
            if (toId != 0) this.hashKey32 = this.hashKey32 ^ this.hashKey32Table[toId][move.to.x][move.to.y];
            this.hashCheck[(move.to.x*10+move.to.y)]=this.hashCheck[move.from.x*10+move.from.y];
            this.hashKey32 = this.hashKey32 ^ this.hashKey32Table[fromId][move.to.x][move.to.y];
            this.hashCheck[move.from.x*10+move.from.y]="0";
        },
        hashUnMakeMove:function(move,chess,map){
            var toId = this.convertChessToNumber(map[move.to.x][move.to.y]),
                chessId=this.convertChessToNumber(chess);
            this.hashKey32 = this.hashKey32 ^ this.hashKey32Table[toId][move.from.x][move.from.y];
            this.hashCheck[move.from.x*10+move.from.y]=this.hashCheck[move.to.x*10+move.to.y];
            this.hashKey32 = this.hashKey32 ^ this.hashKey32Table[toId][move.to.x][move.to.y];
            if (chessId) this.hashKey32 = this.hashKey32 ^ this.hashKey32Table[chessId][move.to.x][move.to.y];
            this.hashCheck[move.to.x*10+move.to.y]=String.fromCharCode(toId+56);
        },
        lookUpHashTable:function(alpha, beta, depth, isRedTurn){
            var x = this.hashKey32 & 0xFFFFF,
                pht = this.TT[isRedTurn][x];

            if (pht.depth >= depth && pht.checkSum == this.hashCheck){
                switch (pht.entryType) {
                    case "exact":
                        return pht.eval;
                    case "lowerBound":
                        if (pht.eval >= beta)
                            return (pht.eval);
                        else
                            break;
                    case "upperBound":
                        if (pht.eval <= alpha)
                            return (pht.eval);
                        else
                            break;
                }
            }
            return 66666;
        },
        enterHashTable:function(entryType, eval, depth,isRedTurn){
            var x = this.hashKey32 & 0xFFFFF;//二十位哈希地址
            this.TT[isRedTurn][x].checkSum = this.hashCheck;
            this.TT[isRedTurn][x].entryType = entryType;
            this.TT[isRedTurn][x].eval = eval;
            this.TT[isRedTurn][x].depth = depth;
        }
    });
    // Alpha-beta pruning algorithm engine
    var AlphaBetaEngine=SearchEngine.extend({
        create:function(){
            this._super();
        },
        searchAGoodMove:function(map){
            this.mapCopy(map);
            this.maxDepth = this.searchDepth;
            this.alphabeta(this.maxDepth, -20000, 20000);
            return this.bestMove;
        },
        alphabeta:function(depth, alpha, beta){
            var score=this.isGameOver(this.currentMap, depth);
            if (score != 0) return score;
            if (depth <= 0) // valuations take a leaf node
                return this.evaluator.evaluate(this.currentMap, (this.maxDepth-depth)%2);//isRedTurn=(this.maxDepth-depth)%2

            var count = this.moveGen.createPossibleMove(this.currentMap, depth, (this.maxDepth-depth)%2);

            for (var i=0;i<count;i++)
            {
                var chess = this.makeMove(this.moveGen.moveList[depth][i]);
                score = -this.alphabeta(depth - 1, -beta, -alpha);
                this.unMakeMove(this.moveGen.moveList[depth][i],chess);

                if (score > alpha)
                {
                    alpha = score;
                    if(depth == this.maxDepth)
                        this.bestMove = this.moveGen.moveList[depth][i];
                }
                if (alpha >= beta) break;
            }
            return alpha;
        }
    });
    // Minimal window search history inspired + + + iterative deepening permutation table optimization
    var NegaScout_TT_HH=SearchEngine.extend({
        TT:null,
        HH:null,
        timeCount:null,
        timeLimit:null,
        create:function(){
            this._super();
            this.TT=new TranspositionTable();
            this.HH=new HistoryHeuristic();
        },
        searchAGoodMove:function(map){
            // Best Walking backup
            var backupBestMove=new Object();
            // Search depth stop time
            var stopDepth=0;
            // Time limit
            this.timeLimit=1500;
            // Copy checkerboard value
            // - Due to stop in the middle, you can not return to the initial state, and therefore the value of copies required, to avoid affecting the chess game conduct
            this.mapCopy(map);
            // Calculate the initial value
            this.TT.calculateInitHashKey(this.currentMap);
            this.HH.resetHistoryTable();
            this.timeCount=(new Date()).getTime();
            for (this.maxDepth = 1; this.maxDepth <= this.searchDepth; this.maxDepth++){
                if(this.negaScout(this.maxDepth, -20000, 20000)!=66666){
                    backupBestMove.from=new Point(this.bestMove.from.x,this.bestMove.from.y);
                    backupBestMove.to=new Point(this.bestMove.to.x,this.bestMove.to.y);
                    backupBestMove.score=this.bestMove.score;
                    stopDepth=this.maxDepth;
                }
            }
            document.getElementById("TimeCost").innerHTML=(new Date().getTime()-this.timeCount).toString();
            document.getElementById("depth").innerHTML=stopDepth-1;
            return backupBestMove;
        },
        negaScout:function(depth, alpha, beta){
            // If the outcome had been, then do not need to search
            var score = this.isGameOver(this.currentMap, depth);
            if (score != 0) return score;
            // Determine the party line chess
            var isRedTurn = (this.maxDepth-depth)%2;
            // Check the hash table, if there are records, the direct return
            score = this.TT.lookUpHashTable(alpha, beta, depth, isRedTurn);
            if (score != 66666) return score;
            // Leaf node, valuation
            if (depth <= 0){
                score = this.evaluator.evaluate(this.currentMap, isRedTurn );
                this.TT.enterHashTable("exact", score, depth, isRedTurn );
                return score;
            }
            // The first layer node layer has timed out, then stop the search continues. If it is not first-tier nodes, you should continue to complete the search.
            if (depth == this.maxDepth){
                if ((new Date().getTime()) - this.timeCount >= this.timeLimit)
                    return 66666;
            }
            // Generate child nodes
            var count = this.moveGen.createPossibleMove(this.currentMap, depth, isRedTurn);
            // Remove the history scores sort order to adjust the child nodes
            for (i=0;i<count;i++)
                this.moveGen.moveList[depth][i].score = this.HH.getHistoryScore(this.moveGen.moveList[depth][i]);
            this.HH.mergeSort(this.moveGen.moveList[depth], count, 0);
            // Search for
            var bestmove=-1;
            var a = alpha,
                b = beta;
            var isEvalExact = 0;
            // Traverse the child nodes
            for (var i = 0; i < count; ++i ) {
                // Compute the hash value
                this.TT.hashMakeMove(this.moveGen.moveList[depth][i], this.currentMap);
                // Move the pieces, transferred to a new state
                var target = this.makeMove(this.moveGen.moveList[depth][i]);
                // The first node is a full-window search
                var t = -this.negaScout(depth-1 , -b, -a );
                // After the first node, if fail hight, then search again
                if (t > a && t < beta && i > 0)
                {
                    a = -this.negaScout (depth-1, -beta, -t );
                    isEvalExact = 1;
                    if(depth == this.maxDepth)
                        this.bestMove = this.moveGen.moveList[depth][i];
                    bestmove = i;
                }

                // Undo Hash
                this.TT.hashUnMakeMove(this.moveGen.moveList[depth][i], target, this.currentMap);
                // Restore the state
                this.unMakeMove(this.moveGen.moveList[depth][i],target);
                // Hit, save
                if (a < t)
                {
                    isEvalExact = 1;
                    a=t;
                    if(depth == this.maxDepth)
                        this.bestMove = this.moveGen.moveList[depth][i];
                }
                if ( a >= beta )
                {
                    // Hit, save
                    this.TT.enterHashTable("lowerBound", a, depth, isRedTurn);
                    // History
                    this.HH.enterHistoryScore(this.moveGen.moveList[depth][i], depth);
                    return a;// pruning
                }
                b = a + 1;// set the new empty window
            }
            // Moves to join the best history
            if (bestmove != -1)
                this.HH.enterHistoryScore(this.moveGen.moveList[depth][bestmove], depth);
            // Search result is stored in the replacement table
            if (isEvalExact)
                this.TT.enterHashTable("exact", a, depth, isRedTurn);
            else
                this.TT.enterHashTable("upperBound", a, depth, isRedTurn);
            return a;
        }
    });
    // Valuation engine
    var Evaluation=Class.extend({
        constant:{
            baseValue:{
                R_PAWN:100,
                R_BISHOP:250,
                R_ELEPHANT:250,
                R_CAR:500,
                R_HORSE:350,
                R_CANON:350,
                R_KING:10000,

                B_PAWN:100,
                B_BISHOP:250,
                B_ELEPHANT:250,
                B_CAR:500,
                B_HORSE:350,
                B_CANON:350,
                B_KING:10000
            },
            flexValue:{
                R_PAWN:15,
                R_BISHOP:1,
                R_ELEPHANT:1,
                R_CAR:6,
                R_HORSE:12,
                R_CANON:6,
                R_KING:0,

                B_PAWN:15,
                B_BISHOP:1,
                B_ELEPHANT:1,
                B_CAR:6,
                B_HORSE:12,
                B_CANON:6,
                B_KING:0
            },
            BPawn:[
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [ 70, 70, 70, 70, 70, 70, 70, 70, 70],
                [ 70, 90,110,110,110,110,110, 90, 70],
                [ 90, 90,110,120,120,120,110, 90, 90],
                [ 90, 90,110,120,120,120,110, 90, 90],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0]
            ],
            RPawn:[
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [ 90, 90,110,120,120,120,110, 90, 90],
                [ 90, 90,110,120,120,120,110, 90, 90],
                [ 70, 90,110,110,110,110,110, 90, 70],
                [ 70, 70, 70, 70, 70, 70, 70, 70, 70],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0],
                [  0,  0,  0,  0,  0,  0,  0,  0,  0]
            ]
        },
        attackPos:null,
        guardPos:null,
        flexibilityPos:null,
        chessValue:null,
        posCount:0,
        relatePos:null,
        create:function(){
            this.relatePos=new Array();
            this.posCount=0;
            // Initialize
            this.chessValue=new Array();
            this.attackPos=new Array();// threat degree
            this.guardPos=new Array();// Protected degree
            this.flexibilityPos=new Array();// flexibility
            for(var i=0;i<9;++i){
                this.chessValue[i]=new Array();
                this.attackPos[i]=new Array();
                this.guardPos[i]=new Array();
                this.flexibilityPos[i]=new Array();
                for(var j=0;j<10;++j){
                    this.chessValue[i][j]=0;
                    this.attackPos[i][j]=0;
                    this.guardPos[i][j]=0;
                    this.flexibilityPos[i][j]=0;
                }
            }
        },
        evaluate:function(map, isRedTurn){
            for(var i=0;i<9;++i){
                for(var j=0;j<10;++j){
                    this.chessValue[i][j]=0;
                    this.attackPos[i][j]=0;
                    this.guardPos[i][j]=0;
                    this.flexibilityPos[i][j]=0;
                }
            }
            // First pass, computing protection, threats, flexibility
            for(var j = 0; j < 10; ++j)
                for(var i = 0; i < 9; ++i)
                {
                    if(map[i][j] != null)
                    {
                        var chess = map[i][j];
                        this.getRelatePiece(map, new Point(i,j));// all positions
                        //alert(this.posCount);
                        for (var k = 0; k < this.posCount; ++k)
                        {
                            var target = map[this.relatePos[k].x][this.relatePos[k].y];
                            if (target == null)
                            {
                                ++this.flexibilityPos[i][j];
                            }
                            else
                            {
                                if (chess.camp==target.camp)// protection
                                {
                                    ++this.guardPos[this.relatePos[k].x][this.relatePos[k].y];
                                }
                                else// threat
                                {
                                    ++this.flexibilityPos[i][j];
                                    // Threat hundredth degree = 3 + pawn value difference
                                    this.attackPos[this.relatePos[k].x][this.relatePos[k].y] += 3+Math.floor((this.constant.baseValue[target.type] - this.constant.baseValue[chess.type])*0.01);
                                    if(target.type=="R_KING")
                                        if (!isRedTurn) return 18888;// fail
                                        else if(target.type=="B_KING")
                                            if (isRedTurn) return 18888;// fail
                                }
                            }
                        }
                    }
                }
            // Second scan, the basic value, flexibility, value, threats, protection
            var BRKing=["B_KING","R_KING"];
            for(var j = 0; j < 10; ++j)
                for(var i = 0; i < 9; ++i)
                {
                    if(map[i][j] != null)
                    {
                        var chess = map[i][j];
                        var halfValue = Math.floor(this.constant.baseValue[chess.type]/16);// one of the fundamental values of 16 points as the basic unit
                        this.chessValue[i][j] += this.constant.baseValue[chess.type];// basic values
                        this.chessValue[i][j] += this.constant.flexValue[chess.type]     * this.flexibilityPos[i][j];// The value of flexibility
                        if(chess.type=="R_PAWN")this.chessValue[i][j] += this.constant.RPawn[j][i];// soldiers added
                        if(chess.type=="B_PAWN")this.chessValue[i][j] += this.constant.BPawn[j][i];// Death of added value

                        if (this.attackPos[i][j])// threatened scores decrease
                        {
                            if ((chess.isRed() && isRedTurn) || (!chess.isRed() && !isRedTurn))// threatened and their own path
                            {
                                if (chess.type == BRKing[isRedTurn])
                                {
                                    this.chessValue[i][j]-= 20;// own will be a threat, the other favorable
                                }
                                else
                                {
                                    this.chessValue[i][j] -= halfValue * 2;// Low Threat
                                    if (this.guardPos[i][j])this.chessValue[i][j] += halfValue;// protected, threatening halved
                                }
                            }
                            else// threatened and the other playing chess
                            {
                                if (chess.type == BRKing[isRedTurn])
                                    return 18888;// own will be a threat, the other playing chess, failure
                                this.chessValue[i][j] -= halfValue*10;// high threat
                                if (this.guardPos[i][j])this.chessValue[i][j] += halfValue*9;// to be protected, to eliminate the threat of majority
                            }
                            // Is the greater the degree of threat, the more favorable the other side, where the child is used to calculate the dollar
                            this.chessValue[i][j] -= this.attackPos[i][j];
                        }
                        else
                        {
                            if (this.guardPos[i][j])this.chessValue[i][j] += 5;// not threatened, only protected, add a little points
                        }
                    }
                }
            /// Scan the third time, the total value
            var BRValue=[0,0];
            for(j = 0; j < 10; ++j)
                for(i = 0; i < 9; ++i)
                {
                    var chess = map[i][j];
                    if (chess != null)
                        BRValue[chess.isRed()?1:0]+=this.chessValue[i][j];
                }
            // Returns valuation
            return BRValue[isRedTurn] - BRValue[isRedTurn^1];
        },
        getRelatePiece:function(map,pos){
            this.posCount = 0;
            this.relatePos.length=0;
            chess = map[pos.x][pos.y];
            if(chess.type=="R_KING"||chess.type=="B_KING")this.genKingMove(map,pos);
            else if(chess.type=="R_BISHOP"||chess.type=="B_BISHOP")this.genBishopMove(map,pos);
            else if(chess.type=="R_ELEPHANT"||chess.type=="B_ELEPHANT")this.genElephantMove(map,pos);
            else if(chess.type=="R_HORSE"||chess.type=="B_HORSE")this.genHorseMove(map,pos);
            else if(chess.type=="R_CAR"||chess.type=="B_CAR")this.genCarMove(map,pos);
            else if(chess.type=="R_PAWN"||chess.type=="B_PAWN")this.genPawnMove(map,pos);
            else if(chess.type=="R_CANON"||chess.type=="B_CANON")this.genCanonMove(map,pos);
            return this.posCount;
        },
        genKingMove:function(map, pos){
            var x,y,k;
            var dx=[-1,0,1,0],
                dy=[0,-1,0,1];
            var chess=map[pos.x][pos.y];
            for (k=0;k<4;++k)
            {
                x=pos.x+dx[k],y=pos.y+dy[k];
                if (y<0 || y>=10 || x<0 || x>=9) continue;
                if ((x<3 || x>5) || (chess.type=="R_KING" && y<7) || (chess.type=="B_KING" && y>2))continue;
                this.addPoint(new Point(x,y));
            }
            if (chess.type=="R_KING")
            {
                for(x=pos.x,y=pos.y-1;y>=0 && map[x][y]==null;--y);
                if(y>=0 && map[x][y]=="B_KING")this.addPoint(new Point(x,y));
            }
            else if (chess.type=="B_KING")
            {
                for(x=pos.x,y=pos.y+1;y<10 && map[x][y]==null;++y);
                if(y<10 && map[x][y]=="R_KING")this.addPoint(new Point(x,y));
            }
        },
        genBishopMove:function(map, pos){
            var x,y,k;
            var dx=[-1,1,1,-1],
                dy=[-1,-1,1,1];
            var chess=map[pos.x][pos.y];
            for (k=0;k<4;++k)
            {
                x=pos.x+dx[k],y=pos.y+dy[k];
                if (y<0 || y>=10 || x<0 || x>=9) continue;
                if ((x<3 || x>5) || (chess.type=="R_BISHOP" && y<7) || (chess.type=="B_BISHOP" && y>2))continue;
                this.addPoint(new Point(x,y));
            }
        },
        genElephantMove:function(map, pos, ply){
            var x,y,k;
            var dx=[-2,2,2,-2],
                dy=[-2,-2,2,2];
            var chess=map[pos.x][pos.y];
            for (k=0;k<4;++k)
            {
                x=pos.x+dx[k],y=pos.y+dy[k];
                if (y<0 || y>=10 || x<0 || x>=9) continue;
                if ((chess.type=="R_ELEPHANT" && y<5) || (chess.type=="B_ELEPHANT" && y>4))continue;
                if(map[Math.floor((pos.x+x)/2)][Math.floor((pos.y+y)/2)] != null)continue;
                this.addPoint(new Point(x,y));
            }
        },
        genHorseMove:function(map, pos){
            var x,y,k;
            var dx=[1,2,2,1,-1,-2,-2,-1],
                dy=[-2,-1,1,2,2,1,-1,-2];
            var chess=map[pos.x][pos.y];
            for (k=0;k<8;++k)
            {
                x=pos.x+dx[k],y=pos.y+dy[k];
                if (y<0 || y>=10 || x<0 || x>=9) continue;
                if(map[pos.x+parseInt(dx[k]/2)][pos.y+parseInt(dy[k]/2)] != null)continue;
                this.addPoint(new Point(x,y));
            }
        },
        genCarMove:function(map, pos){
            var x,y;
            var chess = map[pos.x][pos.y];
            for(x=pos.x+1,y=pos.y;x<9 && null == map[x][y];++x)this.addPoint(new Point(x, y));
            if(x<9) this.addPoint(new Point(x, y));
            for(x=pos.x-1,y=pos.y;x >= 0 && null == map[x][y];--x)this.addPoint(new Point(x, y));
            if(x>=0) this.addPoint(new Point(x, y));
            for(x=pos.x,y=pos.y+1;y < 10 && null == map[x][y];++y) this.addPoint(new Point(x, y));
            if(y<10) this.addPoint(new Point(x, y));
            for(x =pos.x,y =pos.y-1;y>=0 && null == map[x][y];--y) this.addPoint(new Point(x, y));
            if(y>=0) this.addPoint(new Point(x, y));
        },
        genPawnMove:function(map, pos){
            var x,y;
            var chess = map[pos.x][pos.y];
            if((chess.type=="R_PAWN" && pos.y < 5)||(chess.type=="B_PAWN" && pos.y>4))
            {
                y=pos.y,x=pos.x+1;
                if(x < 9) this.addPoint(new Point(x, y));
                x=pos.x-1;
                if(x >= 0) this.addPoint(new Point(x, y));
            }
            x=pos.x;
            y=pos.y+(chess.isRed()?-1:1);
            if ((chess.type=="R_PAWN" && y<0) || (chess.type=="B_PAWN" && y>=10))return;
            this.addPoint(new Point(x, y));
        },
        genCanonMove:function(map, pos){
            var x, y;
            var chess = map[pos.x][pos.y];
            for(x=pos.x+1,y=pos.y;x<9 && null == map[x][y];++x)this.addPoint(new Point(x, y));
            for(++x;x<9 && null == map[x][y];++x);
            if(x<9) this.addPoint(new Point(x, y));
            for(x=pos.x-1,y=pos.y;x>=0 && null == map[x][y];--x) this.addPoint(new Point(x, y));
            for(--x;x>=0 && null == map[x][y];--x);
            if(x>=0) this.addPoint(new Point(x, y));
            for(x=pos.x,y=pos.y+1;y<10 && null == map[x][y];++y) this.addPoint(new Point(x, y));
            for(++y;y<10 && null == map[x][y];++y);
            if(y<10) this.addPoint(new Point(x, y));
            for(x=pos.x,y=pos.y-1;y>=0 && null == map[x][y];--y)this.addPoint(new Point(x, y));
            for(--y;y>=0 && null == map[x][y];--y);
            if(y>=0)this.addPoint(new Point(x, y));
        },
        addPoint:function(pos){
            this.relatePos[this.posCount++] = pos;
        }
    });
    // Game control class
    var GameManager = Class.extend({
        canvas:null,
        board:null,
        create:function(boardId){
            this.canvas = document.getElementById(boardId);
        },
        // Initialize
        init:function () {
            this.board = new Board("chessBoard", this.canvas);
            this.createChesses(this.board);
            this.board.init();
            this.board.show();
            if(campOrder == 0)
                this.board.campOrder =1;
            else this.board.campOrder = 0;
            myBoard = this.board;
        },
        restore:function(){
            this.board.restore();
        },
        createChesses:function(board) {
            {
                (new Chariot("車01", board, campOrder, new Point(0, 9)));
                (new Chariot("車02", board, campOrder, new Point(8, 9)));
                (new Horse("馬01", board, campOrder, new Point(1, 9)));
                (new Horse("馬02", board, campOrder, new Point(7, 9)));
                (new Elephant("相01", board, campOrder, new Point(2, 9)));
                (new Elephant("相02", board, campOrder, new Point(6, 9)));
                (new Guard("士01", board, campOrder, new Point(3, 9)));
                (new Guard("士02", board, campOrder, new Point(5, 9)));
                (new General("帥00", board, campOrder, new Point(4, 9)));
                (new Pawn("兵01", board, campOrder, new Point(0, 6)));
                (new Pawn("兵02", board, campOrder, new Point(2, 6)));
                (new Pawn("兵03", board, campOrder, new Point(4, 6)));
                (new Pawn("兵04", board, campOrder, new Point(6, 6)));
                (new Pawn("兵05", board, campOrder, new Point(8, 6)));
                (new Cannon("炮01", board, campOrder, new Point(1, 7)));
                (new Cannon("炮02", board, campOrder, new Point(7, 7)));
            } {
                (new Chariot("車11", board, 1-campOrder, new Point(0, 0)));
                (new Chariot("車12", board, 1-campOrder, new Point(8, 0)));
                (new Horse("馬11", board, 1-campOrder, new Point(1, 0)));
                (new Horse("馬12", board, 1-campOrder, new Point(7, 0)));
                (new Elephant("象11", board, 1-campOrder, new Point(2, 0)));
                (new Elephant("象12", board, 1-campOrder, new Point(6, 0)));
                (new Guard("仕11", board, 1-campOrder, new Point(3, 0)));
                (new Guard("仕12", board, 1-campOrder, new Point(5, 0)));
                (new General("將10", board, 1-campOrder, new Point(4, 0)));
                (new Pawn("卒11", board, 1-campOrder, new Point(0, 3)));
                (new Pawn("卒12", board, 1-campOrder, new Point(2, 3)));
                (new Pawn("卒13", board, 1-campOrder, new Point(4, 3)));
                (new Pawn("卒14", board, 1-campOrder, new Point(6, 3)));
                (new Pawn("卒15", board, 1-campOrder, new Point(8, 3)));
                (new Cannon("砲11", board, 1-campOrder, new Point(1, 2)));
                (new Cannon("砲12", board, 1-campOrder, new Point(7, 2)));
            }
        }
    });
    return new GameManager(boardId);
}


/**
 * Created by F.U.C.K on 18-Oct-14.
 */
