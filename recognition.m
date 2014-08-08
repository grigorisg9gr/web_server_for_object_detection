function [  ] = recognition( filename,source, destination,extension,category )
% 15 August 2013, greg

startup; %isws na xreiazetai na fortwnontai ligotera arxeia :) 

% fprintf('compiling the code...');
% compile;
a=pwd;
copyfile(sprintf('%s%s',source,filename), a);

if (strcmp(category,'person'))
    fileToLoad='INRIA/inriaperson_final';
else
    fileToLoad=strcat('VOC2010/',category,'_final');
end
try
    load(fileToLoad);
catch e;
      disp('mistake loading file');
      copyfile(sprintf('%s/%s',a,filename),destination); %in case of fail, copy at least the initial image
      exit;
end
%disp('1');
%test(filename, model, 2,destination,extension);
num_dets=2;

im = imread(filename);
% detect objects
%disp('2');
[ds, bs] = imgdetect(im, model, -0.3);
top = nms(ds, 0.5);
top = top(1:min(length(top), num_dets));
ds = ds(top, :);
bs = bs(top, :);
clf;
if model.type == model_types.Grammar
  bs = [ds(:,1:4) bs];
end
%disp('3');
if model.type == model_types.MixStar
  try
      % get bounding boxes
      bbox = bboxpred_get(model.bboxpred, ds, reduceboxes(model, bs));
      bbox = clipboxes(im, bbox);
      top = nms(bbox, 0.5);
      clf;
      h=figure;
      showboxes(im, bbox(top,:));
      saveas(h,sprintf('%s%s',destination,filename),extension);
      disp('processed successfully');
  catch e;
      disp('mistake in the try part (getting bounding boxes)');
      copyfile(sprintf('%s/%s',a,filename),destination); %in case of fail, copy at least the initial image
  end
  if (exist(filename,'file'))
      delete(filename);
  end
  if (exist(sprintf('%s%s','edited_',filename),'file'))
      ;%delete(sprintf('%s%s','edited_',filename));
  end
end
%disp('4');
%close all;
end

